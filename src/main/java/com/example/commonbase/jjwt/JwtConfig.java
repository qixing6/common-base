package com.example.commonbase.jjwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@ConfigurationProperties(prefix="jwt")
@Component
@Data
public class JwtConfig {
    private String secret;
    private long accessExpire=30;
    private long refreshExpire=7;
}
