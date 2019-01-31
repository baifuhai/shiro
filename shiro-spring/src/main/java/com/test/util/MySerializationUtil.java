package com.test.util;

import org.apache.shiro.session.Session;
import org.springframework.util.SerializationUtils;

import java.util.Base64;

public class MySerializationUtil {

    public static String serialize(Session session) {
        return new String(Base64.getEncoder().encode(SerializationUtils.serialize(session)));
    }

    public static Session deserialize(String sessionStr) {
        return (Session) SerializationUtils.deserialize(Base64.getDecoder().decode(sessionStr.getBytes()));
    }

}
