package MyServer.service;

import MyServer.bean.Pic;
import MyServer.bean.Style;
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
        System.out.println("搜索");
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
        }else{
            result = jdbcTemplate.query("select * from userfile where id<? order by id desc limit 5", new Object[] {
                    id }, new BeanPropertyRowMapper(Pic.class));
        }

        if (result == null || result.isEmpty()) {
            return null;
        }
        return result;
    }



}
