package com.test.realm;

import com.test.bean.User;
import com.test.service.UserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

public class MyRealm extends AuthorizingRealm implements InitializingBean {

	private String hashAlgorithmName;

	@Autowired
	private UserService userService;

	@Override
	public void afterPropertiesSet() throws Exception {
		HashedCredentialsMatcher hashedCredentialsMatcher = (HashedCredentialsMatcher) getCredentialsMatcher();
		hashAlgorithmName = hashedCredentialsMatcher.getHashAlgorithmName();
	}

	//用于认证的方法
	@Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("MyRealm [" + hashAlgorithmName + "] doGetAuthenticationInfo...");

		//1. 把 AuthenticationToken 转换为 UsernamePasswordToken
		UsernamePasswordToken upToken = (UsernamePasswordToken) authenticationToken;

		//2. 从 UsernamePasswordToken 中获取 username
		String username = upToken.getUsername();

		//3. 调用数据库的方法，从数据库中查询 username 对应的用户记录
		User user = userService.getByUsername(username);

		//4. 若用户不存在，则抛出 UnknownAccountException 异常
		if (user == null) {
			throw new UnknownAccountException("用户不存在");
		}

		//5. 抛出其他的 AuthenticationException 异常
		if (user.isLocked()) {
			throw new LockedAccountException("用户被锁定");
		}

		//6. 构建 AuthenticationInfo 对象并返回，通常使用的实现类 SimpleAuthenticationInfo
		//1). principal: 认证的实体信息，可以是 username，也可以是数据表对应的用户的实体类对象
		Object principal = username;

		//2). credentials: 密码
		Object credentials = user.getPassword();

		//3). 盐值
		ByteSource credentialsSalt = ByteSource.Util.bytes(username);

		//4). realmName: 当前 realm 对象的 name，调用父类的 getName() 方法即可
		String realmName = getName();

		//SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(principal, credentials, realmName);
		SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(principal, credentials, credentialsSalt, realmName);

        return info;
    }

	//用于授权的方法
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		System.out.println("MyRealm [" + hashAlgorithmName + "] doGetAuthorizationInfo...");

		//从 PrincipalCollection 中获取登录用户的信息，再从数据库中查询用户的角色
		Object principal = principals.getPrimaryPrincipal();
		Set<String> roles = new HashSet<>(userService.getRolesByUsername(principal.toString()));

		return new SimpleAuthorizationInfo(roles);
	}

}
