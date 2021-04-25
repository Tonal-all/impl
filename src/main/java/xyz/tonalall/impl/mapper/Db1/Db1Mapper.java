package xyz.tonalall.impl.mapper.Db1;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import xyz.tonalall.impl.entity.AgeTool;
import xyz.tonalall.impl.entity.Station;

import java.util.List;
import java.util.Map;
import java.util.Set;


@Mapper
@Component
public interface Db1Mapper {
List<Station> findByTime(String table, String colon);
    //@Select("select linename,flow form ${table} where start_time = #{time}")
    List<Map<String, Object>> findLine(String table,String time);

    List<AgeTool>  findAgeIn(String time);
    List<AgeTool> findAgeOut(String time);

    List<Map<String, Object>> find3Line(String table,String time);




}
