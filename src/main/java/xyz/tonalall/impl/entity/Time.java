package xyz.tonalall.impl.entity;

import lombok.*;

import java.net.ServerSocket;

@ToString
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Time {
    private String year;
    private String month;
    private String day;
    private String hour;
    private String minute;
}
