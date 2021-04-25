package xyz.tonalall.impl.mapper.Db3;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import xyz.tonalall.impl.entity.Station;

import java.util.List;

@Mapper
@Component
public interface Db3Mapper {

    List<Station> findAll();

}
