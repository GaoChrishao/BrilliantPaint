package com.gaoch.brilliantpic.myclass;

import java.io.Serializable;

import androidx.annotation.Keep;

@Keep
public class Comment implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private Long fileid;
    private Long account;
    private String username;
    private String userpic;
    private Long time;
    private String content;
    public Comment(){
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserpic() {
        return userpic;
    }

    public void setUserpic(String userpic) {
        this.userpic = userpic;
    }
}
