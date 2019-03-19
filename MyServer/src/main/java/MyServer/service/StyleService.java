package MyServer.service;

import MyServer.ConstValue;
import MyServer.bean.Style;
import MyServer.bean.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StyleService {
    @Autowired
    private JdbcTemplate jdbcTemplate;


    public List<Style> getAll(){
        List<Style> styleList = jdbcTemplate.query("select * from styles", new BeanPropertyRowMapper(Style.class));
        for(int i=0;i<styleList.size();i++){
            Style style=styleList.get(i);
            style.setPicurl(ConstValue.img_model_url+style.getPicurl());
            styleList.set(i,style);
        }
        return styleList;
    }

    public List<Style> search(String type){
        List<Style> styleList = jdbcTemplate.query("select * from styles where type = ?", new Object[] {
                type }, new BeanPropertyRowMapper(Style.class));
        for(int i=0;i<styleList.size();i++){
            Style style=styleList.get(i);
            style.setPicurl(ConstValue.img_model_url+style.getPicurl());
            styleList.set(i,style);
        }
        return styleList;
    }

}
