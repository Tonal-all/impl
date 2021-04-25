package xyz.tonalall.impl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;


import xyz.tonalall.impl.mapper.Db2.UserMapper;
import xyz.tonalall.impl.service.UserService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


@RestController
public class UserController {
    SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//    @Autowired
//    JdbcTemplate jdbcTemplate;
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;


    @GetMapping(value = "/sou/lineFlow/{data}")
    public String[] lineFlow(@RequestParam(value = "data",required = false,defaultValue = "null") String data) throws ParseException {
        Date date1;
        if(data.equals("null")){
            date1 = new Date();
            data = ft.format(date1);
        }else {
           date1 = ft.parse(data);
        }
        int year = Integer.parseInt(data.substring(0, 4));
        int mouth = Integer.parseInt(data.substring(5,7));
        int day = Integer.parseInt(data.substring(8,10));
        int h = Integer.parseInt(data.substring(11,13));
        int min = Integer.parseInt(data.substring(14,16));
        if (year == 2020 && mouth>0&&mouth<8){

        }


        return null;

    }
    @PostMapping(value = "/sou/lineFlow")
    public String[] lineFlowA(){
        return null;
    }
}
