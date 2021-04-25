package xyz.tonalall.impl.service;

import xyz.tonalall.impl.entity.Station;
import xyz.tonalall.impl.entity.User;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface UserService {

    String login(HttpServletResponse response, User user, String password);

    List<Map<String, Object>> findLine(String table, String time);

    Float getOverallFlow(List<Map<String, Object>> maps);

    String getHighestTime(String time);

    List<Station> findAll(String table,String time);

    List<Map<String,Object>> getHotPowerGraph(List<Station> stations);

    List<Map<String, Object>> getHighestDist4(List<Station> stations);

    Map<String,Object> getHighestFlow(List<Station> stations);

    List<Map<String, Object>> getAge(List<Station> stations, String table);


    List<Map<String, Object>> gerSevenFlow(String time);
//    User getByToken(HttpServletResponse response, String token);
//
//    /**
//     * 根据token获得用户信息
//     * @param token
//     * @return
//     */
//    User getByToken(String token);
//
//
//    /**
//     * 获得当前登录用户
//     * @param request
//     * @return
//     */
//    User getLoginUser(HttpServletRequest request);
//
//    /**
//     * 获得当前登录用户
//     * @param request
//     * @param response
//     * @return
//     */
//    User getLoginUser(HttpServletRequest request, HttpServletResponse response);


}
