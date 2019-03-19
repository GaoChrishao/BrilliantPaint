package MyServer.Controller;


import MyServer.bean.FileMessage;
import MyServer.bean.Pic;
import MyServer.bean.User;
import MyServer.service.FileServer;
import MyServer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

@RequestMapping
@RestController
public class FileController {
    /**
     * 客户端发送图片到此函数
     * @param request
     * @param response
     * @throws IOException
     */
    @Autowired
    private FileServer fileServer;


    @RequestMapping(value = "/uploadFile")
    public FileMessage uploadFile(HttpServletRequest request, HttpServletResponse response)
    {
        System.out.println("start------------");
        FileMessage fileMessage= fileServer.process(request);
        System.out.println("end--------------");
        return fileMessage;
    }






}
