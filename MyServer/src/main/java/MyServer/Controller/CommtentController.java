package MyServer.Controller;


import MyServer.bean.Comment;
import MyServer.bean.Pic;
import MyServer.bean.User;
import MyServer.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/comment")
@RestController
public class CommtentController {
    @Autowired
    private CommentService commentService;


    /**
     * 发表评论
     * @param account
     * @param password
     * @param fileid
     * @param content
     * @return
     * @throws Exception
     */

    @RequestMapping(value = "/share")
    public List<Comment> share(
            @RequestParam(value="account",required=true) Long account,
            @RequestParam(value="password",required=true) String password,
            @RequestParam(value="fileid",required=true) Long fileid,
            @RequestParam(value="content",required=true) String content
    ){
        try {
            return commentService.share(account,password,content,fileid);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<Comment>();
        }
    }


    @RequestMapping(value = "/get")
    public List<Comment> share(
            @RequestParam(value="fileid",required=true) Long fileid,
            @RequestParam(value="id",required=false)Long commentid
    ){
        try {
            if(commentid==null){
                return commentService.getNewestComment(-1l,fileid);
            }else{
                //获取指定Pic在id之后的id;
                return commentService.getNewestComment(commentid,fileid);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<Comment>();
        }
    }







}
