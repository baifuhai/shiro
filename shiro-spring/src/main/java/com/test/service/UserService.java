package com.test.service;

import com.test.bean.User;
import com.test.util.MyPasswordSaltUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class UserService implements InitializingBean {

    @Autowired
    private MyPasswordSaltUtil myPasswordSaltUtil;

    private List<User> userList;

    @Override
    public void afterPropertiesSet() throws Exception {
        userList = new ArrayList<>();
        userList.add(new User("admin", myPasswordSaltUtil.encryptPassword("admin", "admin"), false, Arrays.asList("admin", "user")));
        userList.add(new User("user1", myPasswordSaltUtil.encryptPassword("user1", "123"), false, Arrays.asList("user")));
        userList.add(new User("user2", myPasswordSaltUtil.encryptPassword("user2", "123"), true, Arrays.asList("user")));
    }

    public User getByUsername(String username) {
        for (User user : userList) {
            if (username.equals(user.getUsername())) {
                return user;
            }
        }
        return null;
    }

    public List<String> getRolesByUsername(String username) {
        for (User user : userList) {
            if (username.equals(user.getUsername())) {
                return user.getRoles();
            }
        }
        return null;
    }

    @RequiresRoles(value = {"admin"})
    public void test() {
        Session session = SecurityUtils.getSubject().getSession();
        Object value = session.getAttribute("key");
        Object value2 = session.getAttribute("key2");
        System.out.println("RequiresRoles test: " + value);
        System.out.println("RequiresRoles test: " + value2);
    }

}
