package com.test.filter;

import com.google.gson.Gson;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class MyFilter extends FormAuthenticationFilter {

    @Override
    protected boolean onAccessDenied(ServletRequest req, ServletResponse resp) throws Exception {
        HttpServletResponse response = (HttpServletResponse) resp;

        response.setHeader("Content-Type", "application/json; charset=UTF-8");

        Map<String, Object> map = new HashMap<>();
        map.put("code", "500");
        map.put("message", "not login");

        PrintWriter out = response.getWriter();
        out.write(new Gson().toJson(map));
        out.flush();
        out.close();

        return false;
    }

}
