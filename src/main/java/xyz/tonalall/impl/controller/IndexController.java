package xyz.tonalall.impl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.tonalall.impl.entity.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/index")
public class IndexController {


    @RequestMapping("/getUser")
    public User getName(HttpServletRequest request) {

        //通过request来获取保存在session中的用户名
        return (User)request.getSession().getAttribute("user");
    }
}
