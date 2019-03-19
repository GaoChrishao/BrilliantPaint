package MyServer.Controller;


import MyServer.bean.Pic;
import MyServer.bean.Style;
import MyServer.service.StyleService;
import MyServer.service.UserfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/pic")
@RestController
public class UserFileController {
    @Autowired
    private UserfileService userfileService;


    @RequestMapping(value = "/getPic")
    public List<Pic> getPic(@RequestParam(value="account",required=false) String account,
                            @RequestParam(value="id",required=false) Integer id
    )
    {
        if(account!=null){
            return userfileService.getMyPic(account);
        }else if(id!=0){
            return userfileService.getNewestPic(id);
        }else{
            return null;
        }
    }


}
