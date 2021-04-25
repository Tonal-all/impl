package xyz.tonalall.impl.result;

import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import xyz.tonalall.impl.entity.Station;
import xyz.tonalall.impl.mapper.Db1.Db1Mapper;
import xyz.tonalall.impl.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LogResult<T> {
    private Result<T> result;
    private Float overallFlow;
    private String highestTime;
    private Map<String,Object> highestFlow;
    private List<Map<String,Object>> highestDist4;
    private List<Map<String,Object>> hotPowerGraph;
    private List<Map<String,Object>> lineFlow;
    private List<Map<String,Object>> ageMap;
    private List<Map<String, Object>> sevenFlow;

    public static <T> LogResult<T> getLog(@Autowired UserService userService, String time){
//        time: yyyy-MM-dd HH:mm
        LogResult<T> logResult = new LogResult<>();
        int mm = Integer.parseInt(time.substring(14, 16)) / 30 == 0? 0:30;
        String ageTime = Integer.parseInt(time.substring(5, 7))+":"+Integer.parseInt(time.substring(8, 10));

        String table4 = "list4_" + Integer.parseInt(time.substring(5, 7));
        String table3 = "list3_" + Integer.parseInt(time.substring(5, 7));
        String tableTime = Integer.parseInt(time.substring(8, 10)) + ":" + Integer.parseInt(time.substring(11, 13)) + ":" + mm;
        List<Map<String, Object>> line = userService.findLine(table4,tableTime);
        List<Station> stations = userService.findAll(table3, tableTime);  //0
        logResult.setResult(new Result<>(CodeMsg.SUCCESS));
        logResult.setLineFlow(line);
        logResult.setOverallFlow(userService.getOverallFlow(line));
        logResult.setHighestTime(userService.getHighestTime(time));
        logResult.setHighestFlow(userService.getHighestFlow(stations));
        logResult.setHotPowerGraph(userService.getHotPowerGraph(stations));
        logResult.setHighestDist4(userService.getHighestDist4(stations));
        logResult.setAgeMap(userService.getAge(stations,ageTime));
        logResult.setSevenFlow(userService.gerSevenFlow(time));
        return logResult;
    }
    public static <T> LogResult<T> getLog(){
        LogResult<T> result = new LogResult<>();
        result.setResult(new Result<>(CodeMsg.TIME_NO_FIND));
        return result;
    }
}
