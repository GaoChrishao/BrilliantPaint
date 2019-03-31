package com.gaoch.brilliantpic.myclass;

import java.io.Serializable;

import androidx.annotation.Keep;

@Keep
public class Like implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private Long fileid;
    private Long account;
    private Long time;
    private Boolean isLike;

    public Like(){
        id=-1l;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFileid() {
        return fileid;
    }

    public void setFileid(Long fileid) {
        this.fileid = fileid;
    }

    public Long getAccount() {
        return account;
    }

    public void setAccount(Long account) {
        this.account = account;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Boolean getIsLike() {
        return isLike;
    }

    public void setIsLike(Boolean isLike) {
        this.isLike = isLike;
    }
}
