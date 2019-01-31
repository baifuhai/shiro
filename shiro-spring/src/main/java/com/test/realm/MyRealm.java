package com.test.realm;

import com.test.bean.User;
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
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.InitializingBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MyRealm extends AuthorizingRealm implements InitializingBean {

	private String hashAlgorithmName;
	private int hashIterations;

	private List<User> userList;

	@Override
	public void afterPropertiesSet() throws Exception {
		HashedCredentialsMatcher hashedCredentialsMatcher = ((HashedCredentialsMatcher) getCredentialsMatcher());

		hashAlgorithmName = hashedCredentialsMatcher.getHashAlgorithmName();
		hashIterations = hashedCredentialsMatcher.getHashIterations();

		userList = new ArrayList<>();
		userList.add(new User("admin", encryptPassword("admin", "admin"), false, Arrays.asList("admin", "user")));
		userList.add(new User("user1", encryptPassword("user1", "123"), false, Arrays.asList("user")));
		userList.add(new User("user2", encryptPassword("user2", "123"), true, Arrays.asList("user")));
	}

	public String encryptPassword(String username, String password) {
		Object credentials = password;
		Object salt = ByteSource.Util.bytes(username);
		return new SimpleHash(hashAlgorithmName, credentials, salt, hashIterations).toString();
	}

	//用于认证的方法
	@Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("MyRealm [" + hashAlgorithmName + "] doGetAuthenticationInfo...");

		//1. 把 AuthenticationToken 转换为 UsernamePasswordToken
		UsernamePasswordToken upToken = (UsernamePasswordToken) authenticationToken;

		//2. 从 UsernamePasswordToken 中来获取 username
		String username = upToken.getUsername();

		//3. 调用数据库的方法, 从数据库中查询 username 对应的用户记录
		//List<User> userList = getUserList();

		//4. 若用户不存在, 则可以抛出 UnknownAccountException 异常
		boolean flag = false;
		User user2 = null;
		for (User user : userList) {
			if (username.equals(user.getUsername())) {
				flag = true;
				user2 = user;
				break;
			}
		}
		if (!flag) {
			throw new UnknownAccountException("用户不存在");
		}

		//5. 根据用户信息的情况, 决定是否需要抛出其他的 AuthenticationException 异常
		if (user2.isLocked()) {
			throw new LockedAccountException("用户被锁定");
		}

		//6. 根据用户的情况, 来构建 AuthenticationInfo 对象并返回. 通常使用的实现类为: SimpleAuthenticationInfo
		//以下信息是从数据库中获取的
		//1). principal: 认证的实体信息. 可以是 username, 也可以是数据表对应的用户的实体类对象
		Object principal = username;

		//2). credentials: 密码
		Object credentials = user2.getPassword();

		//3). 盐值
		ByteSource credentialsSalt = ByteSource.Util.bytes(username);

		//4). realmName: 当前 realm 对象的 name. 调用父类的 getName() 方法即可
		String realmName = getName();

		//SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(principal, credentials, realmName);
		SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(principal, credentials, credentialsSalt, realmName);

        return info;
    }

	//用于授权的方法
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		System.out.println("MyRealm [" + hashAlgorithmName + "] doGetAuthorizationInfo...");
		Set<String> roles = new HashSet<>();

		Object principal = principals.getPrimaryPrincipal();
		for (User user : userList) {
			if (principal.equals(user.getUsername())) {
				roles.addAll(user.getRoles());
			}
		}

		return new SimpleAuthorizationInfo(roles);
	}

}
