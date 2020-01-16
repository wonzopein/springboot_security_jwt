package com.example.demo.security.jwt;

import com.example.demo.exception.JwtException;
import com.example.demo.security.AuthorizeService;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
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
    public String tokenIssue(String username, List<String> roles){

        Claims claims = Jwts.claims().setSubject(username);
        claims.put("roles", roles);
        claims.put("type", "issue");
        Date now = new Date();


        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + jwtAuthProperties.getAccessTokenExpire()))
                .signWith(SignatureAlgorithm.HS256, jwtAuthProperties.getSecretKey())
                .compact();
    }

    public String refreshTokenIssue(String username, List<String> roles){

        Claims claims = Jwts.claims().setSubject(username);
        claims.put("roles", roles);
        claims.put("type", "refresh");
        Date now = new Date();


        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + jwtAuthProperties.getRefreshTokenExpire()))
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

        //  TODO : API 호출할때마다 DB사용자 정보확인이 필요한경우가 아니라면, JWT 토큰에 필요한 정보를 넣고 사용해도 될듯?
        UserDetails userDetails = authorizeService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
//        return new UsernamePasswordAuthenticationToken(getUsername(token), "", null);
    }

    public String resolveToken(HttpServletRequest request){
        return request.getHeader(JwtAuthProperties.TOKEN_HEADER);
    }

    public boolean validateToken(String token) throws JwtException {

        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(jwtAuthProperties.getSecretKey())
                    .parseClaimsJws(token);


            //  토큰유형(Issue/Refresh)에 따른 구분처리
            //  ex) 갱신토큰(Refresh)으로 인증이 필요한 API 호출시 막기.
            //      갱신토큰으로는 토큰 갱신만 할 수 있음.
            if(!claims.getBody().containsKey("type")
                    || !claims.getBody().get("type").toString().equals("issue")){
                throw new UnsupportedJwtException("UnsupportedJwtException");
            }
            return true;
        }catch (Exception ex){
            throw new JwtException(ex);
        }
//        }catch (SignatureException ex) {
//            throw new JwtException(ex.getMessage());
//        }catch (MalformedJwtException ex){
//            throw ex;
//        }catch (ExpiredJwtException ex){
//            throw ex;
//        } catch (UnsupportedJwtException ex) {
//            throw ex;
//        } catch (IllegalArgumentException ex) {
//            throw ex;
//        }
    }
}
