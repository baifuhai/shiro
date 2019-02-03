package com.test.controller;

import com.test.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping("login")
    public String login(String username, String password/*, Map<String, Object> map*/) {
        Subject currentUser = SecurityUtils.getSubject();
        if (!currentUser.isAuthenticated()) {
            UsernamePasswordToken token = new UsernamePasswordToken(username, password);
            token.setRememberMe(true);
            try {
                currentUser.login(token);
            } catch (AuthenticationException e) {
                e.printStackTrace();
                //map.put("message", e.getMessage());
                //return "redirect:/loginPage";
            }
        }
        return "redirect:/index";
    }

    @RequestMapping("test")
    @ResponseBody
    public String test(HttpServletRequest request, HttpSession session) {
        //servlet的session设值，shiro的session能拿到
        request.getSession().setAttribute("key", "value");
        session.setAttribute("key2", "value2");
        userService.test();
        return "success";
    }

}
