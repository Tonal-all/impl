package xyz.tonalall.impl.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Station implements Comparable<Station>{
//    @JSONField(name = "linename",ordinal = 1)
//    String linename;
//    @JSONField(name = "flow",ordinal = 2)
//    Double flow;
//    String time_start;
    String ID;
    Integer x;
    Integer y;
    String type;
    String name;
    String line;
    String dist;
    Float flow;


    public Station(String ID, String name, String line, String dist) {
        this.ID = ID;
        this.name = name;
        this.line = line;
        this.dist = dist;
    }

    @Override
    public int compareTo(Station o) {
        return o.flow.compareTo(flow);
    }
}
