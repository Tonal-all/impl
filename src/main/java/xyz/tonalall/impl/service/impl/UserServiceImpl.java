package xyz.tonalall.impl.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import xyz.tonalall.impl.entity.AgeTool;
import xyz.tonalall.impl.entity.Station;
import xyz.tonalall.impl.entity.User;
import xyz.tonalall.impl.exception.GlobalException;
import xyz.tonalall.impl.mapper.Db1.Db1Mapper;
import xyz.tonalall.impl.mapper.Db2.UserMapper;
import xyz.tonalall.impl.mapper.Db3.Db3Mapper;
import xyz.tonalall.impl.result.CodeMsg;
import xyz.tonalall.impl.service.UserService;
import xyz.tonalall.impl.utils.RedisUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    public static final String COOKIE_NAME_TOKEN = "token";

    /**
     * token过期时间，2天
     */
    public static final int TOKEN_EXPIRE = 3600 * 24 * 2;


    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    Db1Mapper db1Mapper;

    @Autowired
    Db3Mapper db3Mapper;
    @Autowired
    UserMapper userMapper;

    @Autowired
    JdbcTemplate jdbcTemplate;


    @Override
    public String login(HttpServletResponse response, User user, String password) {
        //判断用户名是否存在


        if (user == null) {
            throw new GlobalException(CodeMsg.USERNAME_NOT_EXIST);
        }

        //验证密码
        String dbPass = user.getPassword();
        if (!password.equals(dbPass)) {
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }

        //生成cookie
        String token = UUID.randomUUID().toString().replace("-", "");

        addCookie(response, token, user);
        return token;
    }

    @Override
    public List<Map<String, Object>> findLine(String table, String time) {

        return db1Mapper.findLine(table, time);
    }

    @Override
    public Float getOverallFlow(List<Map<String, Object>> maps) {
        float f = 0;
        if (!maps.isEmpty()) {
            for (Map<String, Object> map : maps) {
                f += (Float) map.get("flow");
            }
        }
        return f;
    }

    @Override
    public String getHighestTime(String time) {
        String table = "list4_" + Integer.parseInt(time.substring(5, 7));
        int anInt = Integer.parseInt(time.substring(8, 10));
        ArrayList<String> ti = new ArrayList<>();
        ArrayList<Float> num = new ArrayList<>();
        HashMap<Float, String> hashMap = new HashMap<>();
        float a = 0;
        for (int i = 6; i < 23; i++) {
            ti.add(anInt + ":" + i + ":" + "0");
            ti.add(anInt + ":" + i + ":" + "30");
        }
        for (String s : ti) {
            List<Map<String, Object>> line = db1Mapper.findLine(table, s);
            for (Map<String, Object> map : line) {
                a += (Float) map.get("flow");
            }

            num.add(a);

            hashMap.put(a, s);
            a = 0;
        }
        num.sort(Comparator.comparingDouble(Float::doubleValue).reversed());
        return hashMap.get(num.get(0));
    }

    @Override
    public List<Station> findAll(String table, String time) {
        List<Station> stations = db3Mapper.findAll();
        String[] split = time.split(":");
        String s = table.substring(6) + "-" + split[0];
        String s1 = split[1] + ":" + split[2];
        List<Map<String,Object>> list = jdbcTemplate.queryForList("select station,flow from `"+s+"` where time = '"+s1+"'");
        stations.forEach(station -> list.stream().filter(e -> e.get("station").equals(station.getName())).collect(Collectors.toList()).forEach(map->station.setFlow((Float) map.get("flow"))));
        return stations;
    }

    @Override
    public List<Map<String, Object>> getHotPowerGraph(List<Station> stations) {
        List<Map<String, Object>> hotPowerGraph = new ArrayList<>();
        stations.forEach(station -> {
            Map<String, Object> map = new TreeMap<>();
            map.put("id", station.getID());
            map.put("x",station.getX());
            map.put("y",station.getY());
            map.put("level", station.getFlow());
            map.put("line",station.getLine());
            map.put("station", station.getName());
            map.put("dist",station.getDist());
            hotPowerGraph.add(map);
        });
        return hotPowerGraph;
    }

    @Override
    public List<Map<String, Object>> getHighestDist4(List<Station> stations) {
        List<Map<String, Object>> highestDist4 = new ArrayList<>();
        Map<String, Float> map = new HashMap<>();
        for (Station station : stations) {
            if (!map.containsKey(station.getDist())) {
                map.put(station.getDist(), station.getFlow());
            } else {
                map.put(station.getDist(), map.get(station.getDist()) + station.getFlow());
            }
        }

        List<Map.Entry<String, Float>> list = new ArrayList<Map.Entry<String, Float>>(map.entrySet());
        //list.sort()
        //降序排序
        list.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));

        if (list.size() > 0) {
            list.subList(4, list.size()).clear();
        }
        list.forEach(a-> {
            Map<String,Object> map1 = new HashMap<>();
            map1.put("name",a.getKey());
            map1.put("value",a.getValue());
            highestDist4.add(map1);
        });
        return highestDist4;
    }

    @Override
    public Map<String, Object> getHighestFlow(List<Station> stations) {
        Map<String, Object> getHighestFlow = new HashMap<>();
        Collections.sort(stations);
        getHighestFlow.put("name",stations.get(0).getName());
        getHighestFlow.put("value",stations.get(0).getFlow());
        return getHighestFlow;
    }

    @Override
    public List<Map<String, Object>> getAge(List<Station> stations, String table) {
        List<Map<String,Object>> mapList = new ArrayList<>();
        List<AgeTool> ageIn = db1Mapper.findAgeIn(table);
        List<AgeTool> ageOut = db1Mapper.findAgeOut(table);
        stations.forEach(station->{
            Map<String, Object> map = new TreeMap<>();
            map.put("id",station.getID());
            map.put("x",station.getX());
            map.put("y",station.getY());
            map.put("station",station.getName());
            map.put("line",station.getLine());
            map.put("all",null);

            List<AgeTool> list1 = ageIn.stream().filter(list -> list.getStation().equals(station.getName())).collect(Collectors.toList());
            List<AgeTool> list2 = ageOut.stream().filter(list -> list.getStation().equals(station.getName())).collect(Collectors.toList());
            list1.forEach(aa->list2.forEach(bb-> map.put("all",aa.getList(bb))));
            mapList.add(map);
        });
        return mapList;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Map<String, Object>> gerSevenFlow(String time) {
        List<Map<String, Object>> re = new ArrayList<>();
        int mm = Integer.parseInt(time.substring(14, 16)) / 30 == 0? 0:30;
        String preTable = "list4_predict_" + Integer.parseInt(time.substring(5, 7));
        String table4 = "list4_" + Integer.parseInt(time.substring(5, 7));
        String[] a = new String[4];
        a[3] = Integer.parseInt(time.substring(8, 10)) + ":" + Integer.parseInt(time.substring(11, 13)) + ":" + mm;
        a[1] = Integer.parseInt(time.substring(8, 10)) + ":" + (Integer.parseInt(time.substring(11, 13))-1) + ":" + mm;
        if (mm == 30){
            a[0] = Integer.parseInt(time.substring(8, 10)) + ":" + (Integer.parseInt(time.substring(11, 13))-1)+ ":" + 0;
            a[2] = Integer.parseInt(time.substring(8, 10)) + ":" + Integer.parseInt(time.substring(11, 13))+ ":" + 0;
        }else {
            a[0] = Integer.parseInt(time.substring(8, 10)) + ":" + (Integer.parseInt(time.substring(11, 13))-2)+ ":" + 30;
            a[2] = Integer.parseInt(time.substring(8, 10)) + ":" + (Integer.parseInt(time.substring(11, 13))-1)+ ":" + 30;
        }

        Map<String,Object> map = new HashMap<>();
        map.put("linename","1号线");
        map.put("values",new ArrayList<Object>());
        re.add(map);
        Map<String,Object> map1 = new HashMap<>();
        map1.put("linename","2号线");
        map1.put("values",new ArrayList<Object>());
        re.add(map1);
        Map<String,Object> map2 = new HashMap<>();
        map2.put("linename","3号线");
        map2.put("values",new ArrayList<Object>());
        re.add(map2);
        Map<String,Object> map3 = new HashMap<>();
        map3.put("linename","4号线");
        map3.put("values",new ArrayList<Object>());
        re.add(map3);
        Map<String,Object> map4 = new HashMap<>();
        map4.put("linename","5号线");
        map4.put("values",new ArrayList<Object>());
        re.add(map4);
        Map<String,Object> map5 = new HashMap<>();
        map5.put("linename","10号线");
        map5.put("values",new ArrayList<Object>());
        re.add(map5);
        Map<String,Object> map6 = new HashMap<>();
        map6.put("linename","11号线");
        map6.put("values",new ArrayList<Object>());
        re.add(map6);
        Map<String,Object> map7 = new HashMap<>();
        map7.put("linename","12号线");
        map7.put("values",new ArrayList<Object>());
        re.add(map7);

        for (String s : a) {
//            result.add(db1Mapper.findLine(table4,s));
            db1Mapper.findLine(table4, s).forEach(all-> re.stream().filter(asd -> asd.get("linename").equals(all.get("linename"))).collect(Collectors.toList()).forEach(adf-> {
                List<Object> list = (List<Object>) adf.get("values");
                list.add(all.get("flow"));
//                System.out.println(list);
            }));

        }
        System.out.println(re);
        List<Map<String, Object>> line3 = db1Mapper.find3Line(preTable, a[3]);
        line3.stream().limit(8).collect(Collectors.toList()).forEach(all-> re.stream().filter(asd -> asd.get("linename").equals(all.get("linename"))).collect(Collectors.toList()).forEach(adf-> {
            List<Object> list = (List<Object>) adf.get("values");
            list.add(all.get("flow"));
        }));
        line3.stream().skip(8).limit(8).collect(Collectors.toList()).forEach(all-> re.stream().filter(asd -> asd.get("linename").equals(all.get("linename"))).collect(Collectors.toList()).forEach(adf-> {
            List<Object> list = (List<Object>) adf.get("values");
            list.add(all.get("flow"));
        }));
        line3.stream().skip(16).collect(Collectors.toList()).forEach(all-> re.stream().filter(asd -> asd.get("linename").equals(all.get("linename"))).collect(Collectors.toList()).forEach(adf-> {
            List<Object> list = (List<Object>) adf.get("values");
            list.add(all.get("flow"));
        }));




        return re;
    }
    public static <T> List<T> castList(Object obj, Class<T> clazz)
    {
        List<T> result = new ArrayList<T>();
        if(obj instanceof List<?>)
        {
            for (Object o : (List<?>) obj)
            {
                result.add(clazz.cast(o));
            }
            return result;
        }
        return null;
    }

//    @Override
//    public User getByToken(HttpServletResponse response, String token) {
//        if (StringUtils.isEmpty(token)) {
//            return null;
//        }
//        User user = JSON.parseObject(redisUtil.get(COOKIE_NAME_TOKEN + "::" + token), User.class);
//        //重置有效期
//        if (user == null) {
//            throw new GlobalException(CodeMsg.USER_NOT_LOGIN);
//        }
//        if (response != null) {
//            addCookie(response, token, user);
//        }
//        return user;
//    }
//
//    @Override
//    public User getByToken(String token) {
//        return getByToken(null, token);
//    }
//
//    @Override
//    public User getLoginUser(HttpServletRequest request) {
//        return getLoginUser(request, null);
//    }
//
//    @Override
//    public User getLoginUser(HttpServletRequest request, HttpServletResponse response) {
//        String paramToken = request.getParameter(UserServiceImpl.COOKIE_NAME_TOKEN);
//        String cookieToken = getCookieValue(request, UserServiceImpl.COOKIE_NAME_TOKEN);
//        if (StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
//            // return null;
//            throw new GlobalException(CodeMsg.USER_NOT_LOGIN);
//        }
//        String token = StringUtils.isEmpty(paramToken) ? cookieToken : paramToken;
//        if (response == null) {
//            return getByToken(token);
//        }
//        return getByToken(response, token);
//    }
//    private String getCookieValue(HttpServletRequest request, String cookiName) {
//        Cookie[] cookies = request.getCookies();
//        if (cookies == null || cookies.length <= 0) {
//            // return null;
//            throw new GlobalException(CodeMsg.TOKEN_INVALID);
//        }
//        for (Cookie cookie : cookies) {
//            if (cookie.getName().equals(cookiName)) {
//                return cookie.getValue();
//            }
//        }
//        return null;
//    }

    private void addCookie(HttpServletResponse response, String token, User user) {
        //将token存入到redis
        //redisUtil.set(COOKIE_NAME_TOKEN + "::" + token, JSON.toJSONString(user), TOKEN_EXPIRE);
        //将token写入cookie
        System.out.println(token);
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
        cookie.setMaxAge(TOKEN_EXPIRE);
        cookie.setPath("/");
        response.addCookie(cookie);
    }


}
