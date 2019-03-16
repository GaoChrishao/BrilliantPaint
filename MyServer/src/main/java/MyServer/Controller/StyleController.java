package MyServer.Controller;


import MyServer.bean.Style;
import MyServer.bean.User;
import MyServer.service.StyleService;
import MyServer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/style")
@RestController
public class StyleController {
    @Autowired
    private StyleService styleService;


    @RequestMapping(value = "/all")
    public List<Style> all() throws Exception {
        return styleService.getAll();
    }

    @RequestMapping(value = "/search")
    public List<Style> search(@RequestParam(value="type",required=true) String type) throws Exception {
        return styleService.search(type);
    }

}
