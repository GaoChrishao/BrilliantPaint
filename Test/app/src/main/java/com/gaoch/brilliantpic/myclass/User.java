package com.gaoch.brilliantpic.myclass;

import java.io.Serializable;

import androidx.annotation.Keep;

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
    private int exp;
    private String userpic;

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

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public String getUserpic() {
        return userpic;
    }

    public void setUserpic(String userpic) {
        this.userpic = userpic;
    }
}
