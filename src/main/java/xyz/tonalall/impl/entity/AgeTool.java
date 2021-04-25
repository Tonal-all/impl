package xyz.tonalall.impl.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AgeTool {
    private String time_start;
    private String station;
    private Integer a1;
    private Integer a2;
    private Integer a3;
    private Integer a4;
    private Integer a5;
    private Integer a6;
    private Integer a7;
    private Integer a8;
    private Integer a9;
    private Integer a10;
    private Integer a11;
    private Integer a12;
    private Integer a13;
    private Integer a14;
    private Integer a15;
    public List<Integer> getList(AgeTool o){
        List<Integer> list = new ArrayList<>();
//        list.add(a1);
//        list.add(a2);
//        list.add(a3);
//        list.add(a4);
//        list.add(a5);
//        list.add(a6);
//        list.add(a7);
//        list.add(a8);
//        list.add(a9);
//        list.add(a10);

        list.add(a11 + o.a11);
        list.add(a12+o.a12);
        list.add(a13+o.a13);
        list.add(a14+o.a14);
        list.add(a15+o.a15);
        return list;
    }
}
