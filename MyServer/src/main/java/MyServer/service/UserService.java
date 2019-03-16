package MyServer.service;

import MyServer.MyClass.Util;
import MyServer.bean.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

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
    public User signin(Long account,String password) throws Exception {
        User user;
        user=get(account,password);
        if(user!=null)return user;
        return new User();
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
        List<User> result = jdbcTemplate.query("select * from userinfo where account = ?", new Object[] {
                account }, new BeanPropertyRowMapper(User.class));
        if (result == null || result.isEmpty()) {
            return null;
        }
        return result.get(0);
    }
}
