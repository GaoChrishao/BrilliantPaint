package MyServer.service;

import MyServer.ConstValue;
import MyServer.MyClass.Util;
import MyServer.bean.Comment;
import MyServer.bean.Pic;
import MyServer.bean.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {
    @Autowired
    private JdbcTemplate jdbcTemplate;


    public List<Comment> share(Long account,String password,String content,Long fileid) throws Exception {
        User user=get(account,password);
        List<Comment>commentList=new ArrayList<>();
        if(user==null||getPic(fileid)==null){
            //评论账号或者评论对象不存在
            return commentList;
        }
        updateUserComments(account);
        addExp(account,ConstValue.exp_comments);
        addComments(account,fileid,content);
        updatePicComments(fileid);

        return getNewestComment(-1l,fileid);
    }




    /**
     * 更新用户评论条数
     * @param account
     * @return
     * @throws Exception
     */
    public void updateUserComments(Long account) throws Exception {
        jdbcTemplate.update("update userinfo set commentsnum=commentsnum+1 where account = ?",account);
    }

    /**
     * 更新Pic评论条数
     * @param fileid
     * @return
     * @throws Exception
     */
    public void updatePicComments(Long fileid) throws Exception {
        jdbcTemplate.update("update userfile set commentsnum=commentsnum+1 where id = ?",fileid);
    }




    /**
     * 查询Pic
     * @param fileid
     * @return
     */
    public Pic getPic(Long fileid) {
        List<Pic> result = jdbcTemplate.query("select * from userfile where id = ?", new Object[] {
                fileid }, new BeanPropertyRowMapper(Pic.class));
        if (result == null || result.isEmpty()) {
            return null;
        }
        return result.get(0);
    }


    /**
     * 查询用户信息
     * @param account
     * @param password
     * @return
     */
    public User get(Long account, String password) {
        List<User> result = jdbcTemplate.query("select * from userinfo where account = ? and password = ?", new Object[] {
                account,password }, new BeanPropertyRowMapper(User.class));
        if (result == null || result.isEmpty()) {
            return null;
        }
        return result.get(0);
    }
    /**
     * 查询用户信息
     * @param account
     * @return
     */
    public User get(Long account) {
        List<User> result = jdbcTemplate.query("select * from userinfo where account = ?", new Object[] {
                account }, new BeanPropertyRowMapper(User.class));
        if (result == null || result.isEmpty()) {
            return null;
        }
        return result.get(0);
    }


    /**
     * 用户经验增加
     * @param account
     * @return
     */
    public void addExp(Long account,int exp) {
        jdbcTemplate.update("update userinfo set exp=exp+?  where account = ?", exp,account);
    }


    /**
     * 插入评论
     * @param account
     * @param fileid
     * @param content
     */
    public void addComments(Long account,Long fileid,String content){
        jdbcTemplate.update("insert into comments(fileid,account,content,time) values(?,?,?,?)", fileid,account,content,System.currentTimeMillis());
    }

    /**
     * 得到指定id图片的全部评论
      * @param fileid
     * @return
     */
    public List<Comment> getAllComments(Long fileid) {
        List<Comment> result = jdbcTemplate.query("select * from comments where fileid = ?", new Object[] {
                fileid }, new BeanPropertyRowMapper(Comment.class));
        if (result == null || result.isEmpty()) {
            return null;
        }
        for(int i=0;i<result.size();i++){
            Comment comment=result.get(i);
            User u=get(comment.getAccount());
            comment.setUsername(u.getUsername());
            comment.setUserpic(u.getUserpic());
            result.set(i,comment);
        }
        return result;
    }


    public List<Comment>getNewestComment(Long id,Long fileid){
        List<Comment> result=new ArrayList<>();
        if(id==-1l){
            result = jdbcTemplate.query("select * from comments where fileid=? order by id desc limit 20 ",new Object[]{fileid}, new BeanPropertyRowMapper(Comment.class));
            for(int i=0;i<result.size();i++){
                Comment comment=result.get(i);
                User u=get(comment.getAccount());
                comment.setUsername(u.getUsername());
                comment.setUserpic(u.getUserpic());
                result.set(i,comment);

            }
        }else{
            result = jdbcTemplate.query("select * from comments where id<? and fileid=? order by id desc limit 20", new Object[] {
                    id,fileid }, new BeanPropertyRowMapper(Comment.class));
            for(int i=0;i<result.size();i++){
                Comment comment=result.get(i);
                User u=get(comment.getAccount());
                comment.setUsername(u.getUsername());
                comment.setUserpic(u.getUserpic());
                result.set(i,comment);
            }
        }

        if (result == null || result.isEmpty()) {
            Comment comment=new Comment();
            comment.setId(0l);
            result.add(comment);
            return result;
        }

        return result;
    }


}
