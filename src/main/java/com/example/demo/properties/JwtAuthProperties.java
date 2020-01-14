package com.example.demo.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "security.jwt.token")
@Data
public class JwtAuthProperties {

    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer";

    //
    //  암호화 키
    String secretKey;
    //
    //  엑세스 토큰 허용시간
    long accessTokenExpire;
    //
    //  갱신 토큰 허용시간
    long refreshTokenExpire;

}
