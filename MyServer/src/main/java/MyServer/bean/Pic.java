package MyServer.bean;

import java.sql.Time;
import java.sql.Timestamp;

public class Pic {
    private Integer id;
    private Long account;
    private String username;
    private String stylename;
    private String picname;
    private Long time;
    private String userpic;
    private Long likes;
    private Long commentsnum;


    public Pic() {
        id=-1;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getAccount() {
        return account;
    }

    public Long getLikes() {
        return likes;
    }

    public void setLikes(Long likes) {
        this.likes = likes;
    }

    public Long getCommentsnum() {
        return commentsnum;
    }

    public void setCommentsnum(Long commentsnum) {
        this.commentsnum = commentsnum;
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

    public String getStylename() {
        return stylename;
    }

    public void setStylename(String stylename) {
        this.stylename = stylename;
    }

    public String getPicname() {
        return picname;
    }

    public void setPicname(String picname) {
        this.picname = picname;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getUserpic() {
        return userpic;
    }

    public void setUserpic(String userpic) {
        this.userpic = userpic;
    }
}
