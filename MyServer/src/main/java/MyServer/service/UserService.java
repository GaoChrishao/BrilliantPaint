package MyServer.service;

import MyServer.ConstValue;
import MyServer.MyClass.Util;
import MyServer.bean.BasicUserInfo;
import MyServer.bean.Pic;
import MyServer.bean.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private JdbcTemplate jdbcTemplate;


    /**
     * 注册用户
     * @param user
     * @return 成功返回用户信息，失败返回空用户信息
     * @throws Exception
     */
    public User signup(User user) throws Exception {
        int resultCode;
        if(user.getUsername()!=null){
            //新增用户
            Long account;
            while (true){
                //随机出一个不存在用户account
                account=Long.valueOf(Util.getRandom(12));
                if(get(account,null)==null)break;
            }
            resultCode=jdbcTemplate.update("insert into userinfo(account,username,password) values(?,?,?)", account,user.getUsername(),user.getPassword());
            if(resultCode!=-1)
                return get(account,user.getPassword());
        }
        return new User();
    }


    /**
     * 登入
     * @param account
     * @param password
     * @return
     * @throws Exception
     */
    public User login(Long account, String password) throws Exception {
        User user;
        user=get(account,password);
        if(user!=null)return user;
        return new User();
    }



    public  User uploadUserPic(HttpServletRequest request) {
        InputStream is = null;
        try {
            is = request.getInputStream();
        } catch (IOException e) {
            return new User();
        }
        DataInputStream dis = new DataInputStream(is);
        //接受传递过来的用户名与文件名
        String account=request.getParameter("account");
        String password=request.getParameter("password");
        System.out.println(account+":"+password);
        User user=get(Long.valueOf(account),password);
        String preFileName=user.getUserpic();
        if(account==null||account.equals("")||user==null){
            //账号密码错误
            System.out.println("无效请求！");
            return new User();


        }else{
            System.out.println("account:"+account+" password:"+password);
            String afterFileName=account+System.currentTimeMillis()+".jpg";
            System.out.println("开始接受用户头像:"+afterFileName);
            saveUserPic(dis,afterFileName);
            System.out.println("保存用户头像成功"+afterFileName);

            //删除之前保存的用户头像
            File file=new File(ConstValue.img_dir_user+preFileName);
            if(file.exists())file.delete();

            //更新数据库
            addUserFile(account,afterFileName);


            System.out.println(account+":"+password);
            User user1= get(Long.valueOf(account),password);
            if(user1==null)user.setAccount(-1l);
            return user;

        }
    }

    /**
     * 保存接收到的用户头像到本地
     * @param dis,fileName
     * @return 文件的绝对目录
     */
    private String saveUserPic(DataInputStream dis, String fileName) {
        File pf = new File(ConstValue.img_dir_user);
        if(!pf.exists()){
            pf.mkdir();
        }
        File file = new File(ConstValue.img_dir_user+fileName);
        if (file.exists()) {
            file.delete();
        }
        FileOutputStream fps = null;
        try {
            fps = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        int length = -1;

        try {
            while ((length = dis.read(buffer)) != -1) {
                fps.write(buffer, 0, length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fps.flush();
            fps.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }



    /**
     * 更新用户信息
     * @param account
     * @param password
     * @param username
     * @return
     * @throws Exception
     */
    public User update(Long account,String password,String username) throws Exception {
        if(get(account,password)!=null){
            jdbcTemplate.update("update userinfo set username = ?, password = ? where account = ?", username,password,account);
            return get(account,password);
        }
        return new User();
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
    public User get(String account) {
        List<User> result = jdbcTemplate.query("select * from userinfo where account = ?", new Object[] {
                account }, new BeanPropertyRowMapper(User.class));
        if (result == null || result.isEmpty()) {
            return null;
        }
        return result.get(0);
    }


    /**
     * 查询基本用户信息
     * @param account
     * @return
     */
    public BasicUserInfo getBasicInfo(String account) {
        List<User> result = jdbcTemplate.query("select * from userinfo where account = ?", new Object[] {
                account }, new BeanPropertyRowMapper(User.class));
        if (result == null || result.isEmpty()) {
            return null;
        }
        BasicUserInfo basicUserInfo=new BasicUserInfo();
        basicUserInfo.convertUser(result.get(0));
        List<Pic> picList = jdbcTemplate.query("select * from userfile where account=?", new Object[] {
                account }, new BeanPropertyRowMapper(Pic.class));
        for(int i=0;i<picList.size();i++){
            picList.get(i).setUserpic(basicUserInfo.getUserpic());
        }
        basicUserInfo.setPicList(picList);


        return basicUserInfo;
    }





    /**
     * 用户经验增加
     * @param account
     * @return
     */
    public User addExp(String account,int exp) {
        if(get(account)!=null){
            jdbcTemplate.update("update userinfo set exp=exp+?  where account = ?", exp,account);
            return get(account);
        }
        return new User();
    }


    /**
     * 用户经验增加
     * @param account
     * @param userfilename
     * @return
     */
    public void addUserFile(String account,String  userfilename) {
        jdbcTemplate.update("update userinfo set userpic = ?  where account = ?",userfilename,account);
    }
}
