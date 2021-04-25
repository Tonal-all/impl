package xyz.tonalall.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import xyz.tonalall.impl.mapper.Db1.Db1Mapper;
import xyz.tonalall.impl.mapper.Db2.UserMapper;
import xyz.tonalall.impl.mapper.Db3.Db3Mapper;
import xyz.tonalall.impl.service.UserService;


@SpringBootTest
class ImplApplicationTests {

//    @Autowired
//    JdbcTemplate jdbcTemplate;
    @Autowired
UserMapper userMapper;
    @Autowired
    Db1Mapper db1Mapper;
    @Autowired
    UserService userService;
    @Autowired
    Db3Mapper db3Mapper;
    @Test
    void contextLoads() {
//        String sql = "select password from user where username = 'admin';";
//        String user = jdbcTemplate.queryForObject(sql, String.class);
//        System.out.println(user);
//        System.out.println(userMapper.un("admin").toString());
       //db1Mapper.findByTime("list4_6", "5:13:0").forEach(System.out::println);
//        db1Mapper.findLine("list4_6","9:10:0").forEach((a)->{a.forEach((key,value)->{
//            System.out.println(key+"  "+value);
//        });});
        //System.out.println(userService.getOverallFlow(userService.findLine("list4_6", "24:6:0")));
        //System.out.println(userService.getHighestTime("2020-04-16 15:00"));
//        System.out.println(userService.findAll("list3_5","15:12:0"));
        //System.out.println(userMapper.findFlow("list3_5","5:12:0","Sta113"));
        //System.out.println(userService.getHighestDist4(userService.findAll("list3_5","15:12:0")));
        //System.out.println(userService.getHighestFlow(userService.findAll("list3_5", "15:12:0")));
        //userMapper.findFlow("6-15","12:0");
//        System.out.println(db3Mapper.findAll());
//        System.out.println(db1Mapper.findAgeOut("3:15"));
        System.out.println(userService.gerSevenFlow("2020-04-15 15:30"));
    }


}
