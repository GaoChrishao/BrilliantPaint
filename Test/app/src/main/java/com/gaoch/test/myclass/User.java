package com.gaoch.test.myclass;

import androidx.annotation.Keep;

import java.io.Serializable;

/**
 * 实体类
 */
@Keep
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private Long account;
    private String username;
    private String password;

    public User() {
        id= Long.valueOf(0);
        account= Long.valueOf(0);
        username="";
        password="";
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccount() {
        return account;
    }

    public void setAccount(Long account) {
        this.account = account;
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
}
