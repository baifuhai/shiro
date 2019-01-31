package com.test.service;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.session.Session;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @RequiresRoles(value = {"admin"})
    public void test() {
        Session session = SecurityUtils.getSubject().getSession();
        Object value = session.getAttribute("key");
        Object value2 = session.getAttribute("key2");
        System.out.println("RequiresRoles test: " + value);
        System.out.println("RequiresRoles test: " + value2);
    }

}
