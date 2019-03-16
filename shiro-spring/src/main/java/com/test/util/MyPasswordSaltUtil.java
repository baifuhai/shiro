package com.test.util;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

public class MyPasswordSaltUtil {

    private String hashAlgorithmName;
    private int hashIterations;

    public String encryptPassword(String username, String password) {
        Object credentials = password;
        Object salt = ByteSource.Util.bytes(username);
        return new SimpleHash(hashAlgorithmName, credentials, salt, hashIterations).toHex();
    }

    public void setHashAlgorithmName(String hashAlgorithmName) {
        this.hashAlgorithmName = hashAlgorithmName;
    }

    public void setHashIterations(int hashIterations) {
        this.hashIterations = hashIterations;
    }

}
