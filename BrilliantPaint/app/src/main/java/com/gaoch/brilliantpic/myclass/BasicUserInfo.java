package com.gaoch.brilliantpic.myclass;

import java.io.Serializable;
import java.util.List;

import androidx.annotation.Keep;


@Keep
public class BasicUserInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long account;
    private String username;
    private String userpic;
    private int exp;
    private int likes;
    private int commentsnum;


    List<Pic> picList;

    public BasicUserInfo(){
       account=-1l;
    }

    public void convertUser(User user){
        this.account=user.getAccount();
        this.exp=user.getExp();
        this.likes=user.getLikes();
        this.commentsnum =user.getCommentsnum();
        this.username=user.getUsername();
        this.userpic=user.getUserpic();
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

    public String getUserpic() {
        return userpic;
    }

    public void setUserpic(String userpic) {
        this.userpic = userpic;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getCommentsnum() {
        return commentsnum;
    }

    public void setCommentsnum(int commentsnum) {
        this.commentsnum = commentsnum;
    }



    public List<Pic> getPicList() {
        return picList;
    }

    public void setPicList(List<Pic> picList) {
        this.picList = picList;
    }
}
