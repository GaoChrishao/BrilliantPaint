package MyServer.service;

import MyServer.bean.Pic;
import MyServer.bean.Style;
import MyServer.bean.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
public class UserfileService {
    @Autowired
    private JdbcTemplate jdbcTemplate;


    public List<Pic>getMyPic(String account){
        //System.out.println("搜索");
        List<Pic> result = jdbcTemplate.query("select * from userfile where account=?", new Object[] {
                account }, new BeanPropertyRowMapper(Pic.class));
        if (result == null || result.isEmpty()) {
            return null;
        }
        return result;
    }
    public List<Pic>getNewestPic(Integer id){
        List<Pic> result;
        if(id==-1){
            result = jdbcTemplate.query("select * from userfile order by id desc limit 5", new BeanPropertyRowMapper(Pic.class));
            for(int i=0;i<result.size();i++){
                //System.out.println("--------"+result.get(i).getAccount());
                Long account=result.get(i).getAccount();
                if(account==0)break;
                User user=get(account);
                Pic pic=result.get(i);
                pic.setUserpic(user.getUserpic());
                result.set(i,pic);

            }
        }else{
            result = jdbcTemplate.query("select * from userfile where id<? order by id desc limit 5", new Object[] {
                    id }, new BeanPropertyRowMapper(Pic.class));
            for(int i=0;i<result.size();i++){
                //System.out.println("--------"+result.get(i).getAccount());
                Long account=result.get(i).getAccount();
                if(account==0)break;
                User user=get(account);
                Pic pic=result.get(i);
                pic.setUserpic(user.getUserpic());
               result.set(i,pic);

            }
        }

        if (result == null || result.isEmpty()) {
            return null;
        }

        return result;
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



}
