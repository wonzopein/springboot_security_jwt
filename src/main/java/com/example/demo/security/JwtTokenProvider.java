package com.example.demo.security;

import com.example.demo.properties.JwtAuthProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.net.Authenticator;
import java.util.Date;
import java.util.List;

@Component
public class JwtTokenProvider {

    @Autowired
    JwtAuthProperties jwtAuthProperties;

    @Autowired
    AuthorizeService authorizeService;

    /**
     * JWT Access/Refresh 토큰 생성
     * @param username
     * @param roles
     * @return
     */
    public String createToken(String username, List<String> roles){

        Claims claims = Jwts.claims().setSubject(username);
        claims.put("roles", roles);
        Date now = new Date();


        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + jwtAuthProperties.getAccessTokenExpire()))
                .signWith(SignatureAlgorithm.HS256, jwtAuthProperties.getSecretKey())
                .compact();
    }

    public String getUsername(String token){
        return Jwts
                .parser()
                .setSigningKey(jwtAuthProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody().getSubject();
    }

    public Authentication getAuthentication(String token){
        UserDetails userDetails = authorizeService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());

//        return new UsernamePasswordAuthenticationToken(getUsername(token), "", null);
    }

    public String resolveToken(HttpServletRequest request){
        return request.getHeader(JwtAuthProperties.TOKEN_HEADER);
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(jwtAuthProperties.getSecretKey())
                                                .parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        }catch (Exception e){
            return  false;
        }
    }
}
