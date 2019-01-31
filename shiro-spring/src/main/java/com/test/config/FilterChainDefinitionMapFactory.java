package com.test.config;

import java.util.LinkedHashMap;
import java.util.Map;

public class FilterChainDefinitionMapFactory {

    public Map<String, String> getBean() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("/**", "authc");
        return map;
    }

}
