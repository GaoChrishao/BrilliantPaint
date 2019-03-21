package com.gaoch.brilliantpic.myclass;

import java.io.Serializable;

import androidx.annotation.Keep;

@Keep
public class File implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private Long account;
    private String username;
    private String urlpath;
    private String detail;

    public File() {
        id= Long.valueOf(0);
        account= Long.valueOf(0);
        username="";
        urlpath ="";
        detail="";
    }

    @Override
    public String toString() {
        return "File [id=" + id + ", account=" + account + ", username=" + username + ", urlpath="+urlpath+", detail="+detail+"]";
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

    public String getUrlpath() {
        return urlpath;
    }

    public void setUrlpath(String urlpath) {
        this.urlpath = urlpath;
    }
}
