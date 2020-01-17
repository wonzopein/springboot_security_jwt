package com.example.demo.api.auth;

import com.example.demo.api.member.Member;
import com.example.demo.api.member.MemberRepository;
import com.example.demo.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth/token")
public class AuthTokenApi {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    /**
     * JWT 토큰발행
     * @param authJwtRequest
     * @return
     * @throws Exception
     */
    @PostMapping("/issue")
    public AuthJwtDto.Response singin(@RequestBody AuthJwtDto.Request authJwtRequest) {


        Member member = memberRepository.findByUsername(authJwtRequest.getUsername())
                .orElseThrow(()-> new UsernameNotFoundException("USER_NOT_FOUND"));

        if(!passwordEncoder.matches(authJwtRequest.getPassword(), member.getPassword())){
            throw new UsernameNotFoundException("INVALID_CREDENTIALS");
        }


        List<String> roles = new ArrayList<>();
        if(member.getAuthorities() != null){
            roles = member.getAuthorities().stream()
                    .map(i->i.toString())
                    .collect(Collectors.toList());
        }

        AuthJwtDto.Response response = AuthJwtDto.Response.builder()
                .token(jwtTokenProvider.tokenIssue(member.getUsername(), roles))
                .refreshToken(jwtTokenProvider.refreshTokenIssue(member.getUsername(), roles))
                .build();

        return response;
    }

    /**
     * JWT 토큰갱신
     * @param authJwtRequest
     * @return
     */
    @PostMapping("/refresh")
    public AuthJwtDto.Response tokenRefresh(@RequestBody AuthJwtDto.Request authJwtRequest){
        Member member = memberRepository.findByUsername(authJwtRequest.getUsername())
                .orElseThrow(()-> new UsernameNotFoundException("USER_NOT_FOUND"));

        jwtTokenProvider.validateToken(authJwtRequest.getRefreshToken());

        List<String> roles = new ArrayList<>();
        if(member.getAuthorities() != null){
            roles = member.getAuthorities().stream().map(i->i.toString()).collect(Collectors.toList());
        }

        AuthJwtDto.Response response = AuthJwtDto.Response.builder()
                .token(jwtTokenProvider.tokenIssue(member.getUsername(), roles))
                .refreshToken(jwtTokenProvider.refreshTokenIssue(member.getUsername(), roles))
                .build();

        return response;
    }

}
