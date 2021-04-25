package xyz.tonalall.impl.mapper.Db2;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import xyz.tonalall.impl.entity.User;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface UserMapper {
    User un(String username);

//    @Select("select * from 3-15 where time = #{time}")
    List<Map<String,Object>> findFlow(String table, String time);
}
