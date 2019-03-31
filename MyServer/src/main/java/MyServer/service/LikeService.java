package MyServer.service;

import MyServer.ConstValue;
import MyServer.bean.Like;
import MyServer.bean.Pic;
import MyServer.bean.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LikeService {
    @Autowired
    private JdbcTemplate jdbcTemplate;


    public Like like(Long account,String password,Long fileid, boolean isLike)throws Exception{
        User user=get(account,password);
        if(user ==null)return new Like();
        Like like=getLike(fileid,account);
        if(like!=null&&like.getIsLike()!=isLike){
            updateLike(fileid,account,isLike);
            System.out.println("更新");
        }else if(like!=null&&like.getIsLike()==isLike){
            //重复操作
            System.out.println("重复");
            return new Like();

        }else{
            System.out.println("添加");
            addLikes(account,fileid,isLike);
        }

        System.out.println("更新收到赞的用户喜欢");
        Pic pic=getPic(fileid);
        updateUserLikes(pic.getAccount(),isLike);

        System.out.println("更新收到赞的用户经验");
        addExp(pic.getAccount(),isLike,ConstValue.exp_likes);



        return getLike(fileid,account);
    }




    /**
     * 查询Likes
     * @param fileid
     * @return
     */
    public Like getLike(Long fileid,Long account) {
        List<Like> result = jdbcTemplate.query("select * from likes where account = ? and fileid=?", new Object[] {
                account,fileid }, new BeanPropertyRowMapper(Like.class));
        if (result == null || result.isEmpty()) {
            return null;
        }
        return result.get(0);
    }


    /**
     * 更新Likes
     * @param fileid
     * @return
     */
    public void updateLike(Long fileid,Long account,Boolean isLike) {
        jdbcTemplate.update("update likes set isLike=? where account = ? and fileid=?",new Object[]{isLike,account,fileid});
    }







    /**
     * 更新用户收到的赞
     * @param account
     * @return
     * @throws Exception
     */
    public void updateUserLikes(Long account,Boolean isLike) throws Exception {
        if(isLike){
            jdbcTemplate.update("update userinfo set likes=likes+1 where account = ?",account);
        }else{
            jdbcTemplate.update("update userinfo set likes=likes-1 where account = ?",account);
        }

    }

    /**
     * 更新Pic喜欢条数
     * @param fileid
     * @return
     * @throws Exception
     */
    public void updatePicLikes(Long fileid,Boolean isLike) throws Exception {
        if(isLike){
            jdbcTemplate.update("update userfile set likes=likes+1 where id = ?",fileid);
        }else{
            jdbcTemplate.update("update userfile set likes=likes-1 where id = ?",fileid);
        }

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
     * 更新收到赞的用户经验
     * @param account
     * @return
     */
    public void addExp(Long account,boolean isLike, int exp) {
        if(isLike){
            jdbcTemplate.update("update userinfo set exp=exp+?  where account = ?", exp,account);
        }else{
            jdbcTemplate.update("update userinfo set exp=exp-?  where account = ?", exp,account);
        }

    }


    /**
     * 插入赞的几率
     * @param account
     * @param fileid
     * @param isLike
     */
    public void addLikes(Long account,Long fileid,Boolean isLike){
        jdbcTemplate.update("insert into likes(fileid,account,isLike,time) values(?,?,?,?)", fileid,account,isLike,System.currentTimeMillis());
    }






}
