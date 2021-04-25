package xyz.tonalall.impl.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.io.Serializable;


@Component
@Data
@ConfigurationProperties(prefix = "db2")
public class Db2DataSourceProperties implements Serializable {

    private String driverClassName;

    private String url;

    private String username;

    private String password;

    private Integer initialSize;

    private Integer minIdle;

    private Integer maxActive;

    private String platform;

}
