package xyz.tonalall.impl.entity;



import lombok.*;

import java.io.Serializable;


@ToString
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Integer ID;


    private String username;

    private String password;

}
