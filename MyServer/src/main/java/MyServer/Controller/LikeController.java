package MyServer.Controller;


import MyServer.bean.Comment;
import MyServer.bean.Like;
import MyServer.service.CommentService;
import MyServer.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/like")
@RestController
public class LikeController {
    @Autowired
    private LikeService likeService;


    /**
     * 发表评论
     * @param account
     * @param password
     * @return
     * @throws Exception
     */

    @RequestMapping(value = "/shareLike")
    public Like shareLike(
            @RequestParam(value="account",required=true) Long account,
            @RequestParam(value="password",required=true) String password,
            @RequestParam(value="fileid",required=true) Long fileid,
            @RequestParam(value="isLike",required=true) Boolean isLike
    ){
        try {
            return likeService.like(account,password,fileid,isLike);
        } catch (Exception e) {
            e.printStackTrace();
            return new Like();
        }
    }


    @RequestMapping(value = "/getIsLike")
    public Like getIsLike(
            @RequestParam(value="account",required=true) Long account,
            @RequestParam(value="fileid",required=true) Long fileid
    ){
        try {
            return likeService.getIsLike(fileid,account);
        } catch (Exception e) {
            e.printStackTrace();
            return new Like();
        }
    }










}
