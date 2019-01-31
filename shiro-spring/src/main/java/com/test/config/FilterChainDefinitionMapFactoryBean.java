package com.test.config;

import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.FactoryBean;

import java.util.LinkedHashMap;
import java.util.Map;

public class FilterChainDefinitionMapFactoryBean implements FactoryBean<Map<String, String>> {

    @Override
    public Map<String, String> getObject() throws Exception {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("/loginPage", "anon");
        map.put("/login", "anon");
        map.put("/logout", "logout");
        map.put("/admin", "authc, roles[admin]");
        map.put("/user", "authc, roles[user]");
        map.put("/remember", "user");
        map.put("/**", "authc");
        return map;
    }

    @Override
    public Class<?> getObjectType() {
        System.out.println("getObjectType...");
        return new TypeToken<Map<String, String>>(){}.getRawType();
        //return Map.class;
    }

    @Override
    public boolean isSingleton() {
        System.out.println("isSingleton...");
        return true;
    }

}
