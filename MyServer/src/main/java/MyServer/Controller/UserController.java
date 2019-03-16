package MyServer.Controller;


import MyServer.bean.User;
import MyServer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestMapping("/user")
@RestController
public class UserController {
    @Autowired
    private UserService userService;


    @RequestMapping(value = "/signup")
    public User signUp(
            @RequestParam(value="username",required=true) String username,
            @RequestParam(value="password",required=true) String password) throws Exception {
        //接受传递过来的用户名与密码
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        System.out.println("注册操作,account:"+username+" password:"+password);
        return userService.signup(user);
    }

    @RequestMapping(value = "/login")
    public User login(
            @RequestParam(value="account",required=true) String account,
            @RequestParam(value="password",required=true) String password) throws Exception {
        System.out.println("登入操作,account:"+account+" password:"+password);
        return userService.signin(Long.valueOf(account),password);
    }

    @RequestMapping(value = "/update")
    public User update(
            @RequestParam(value="account",required=true) String account,
            @RequestParam(value="password",required=true) String password,
            @RequestParam(value="username",required=true) String username) throws Exception {
        System.out.println("更新操作,account:"+account+" password:"+password+" username:"+username);
        return userService.update(Long.valueOf(account),password,username);
    }
}
