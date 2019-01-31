package com.test.bean;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {

    private int id;
    private String username;
    private String password;
    private boolean locked;
    private List<String> roles;

    public User() {
    }

    public User(String username, String password, boolean locked, List<String> roles) {
        this.username = username;
        this.password = password;
        this.locked = locked;
        this.roles = roles;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

}
