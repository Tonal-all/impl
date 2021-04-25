package xyz.tonalall.impl.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.tonalall.impl.annotation.SystemLog;
import xyz.tonalall.impl.entity.Log;
import xyz.tonalall.impl.entity.Time;
import xyz.tonalall.impl.entity.User;
import xyz.tonalall.impl.enums.LogType;

import xyz.tonalall.impl.mapper.Db2.UserMapper;
import xyz.tonalall.impl.result.LogResult;
import xyz.tonalall.impl.result.Result;
import xyz.tonalall.impl.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


@RestController
@Slf4j
public class LoginController {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;



    @PostMapping("/log")
    @SystemLog(description = "用户登录", type = LogType.LOGIN)
    public Result<String> doLogin(HttpServletResponse response, HttpServletRequest request, @RequestBody Log log) {
        System.out.println(log.getPassword());
        User user = userMapper.un(log.getUsername());

        //用户登录逻辑，返回token
        String token = userService.login(response, user, log.getPassword());
        HttpSession session = request.getSession();
        session.setAttribute("user",user);
        return Result.success(token);
    }
    @GetMapping(value = "/login/{username}/{password}")
    public Result<String> login(HttpServletResponse response, HttpServletRequest request, @PathVariable("username") String username, @PathVariable("password") String password) {
//        String sql = "select password from user where username = '"+log.getUsername()+"'"+";";
//        String user = jdbcTemplate.queryForObject(sql, String.class);
        User user = userMapper.un(username);
        //用户登录逻辑，返回token
        String token = userService.login(response, user, password);
        HttpSession session = request.getSession();
        session.setAttribute("user",user);
        return Result.success(token);

    }


    @PostMapping("/time/json")
    public LogResult<String> doJson(HttpServletResponse response, HttpServletRequest request, @RequestBody Time time) {
//        if (time.getYear().equals("2020")&&Integer.parseInt(time.getMonth())<=7 &&Integer.parseInt(time.getMonth())>=1)
        LogResult<String> result = LogResult.getLog();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Calendar instance = Calendar.getInstance();
        instance.set(Integer.parseInt(time.getYear()), Integer.parseInt(time.getMonth()), Integer.parseInt(time.getDay()), Integer.parseInt(time.getHour()), Integer.parseInt(time.getMinute()));
        Date date = instance.getTime();
        try {
            if (date.after(formatter.parse("2020-03-01 06:00"))&&date.before(formatter.parse("2020-06-30 22:30"))){

                result = LogResult.getLog(userService,formatter.format(date));
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }
}
