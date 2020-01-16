package com.example.demo;

import com.example.demo.member.Member;
import com.example.demo.member.MemberRepository;
import com.example.demo.security.jwt.JwtTokenProvider;
import com.example.demo.security.model.JwtRequest;
import com.example.demo.security.model.JwtResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class HelloContoller {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @PostConstruct
    public void init(){
        Member member = Member.builder()
                        .username("wonzopein")
                        .password(passwordEncoder.encode("1"))
                        .name("오승현")
                        .build();

        memberRepository.save(member);
    }

    @GetMapping("/api/hello")
    public String hello(){
        return "Hello world.";
    }

    @GetMapping("/api/test")
    public String test(){
        return "test!";
    }

    @PostMapping("/api/auth/token/issue")
    public ResponseEntity<?> singin(@RequestBody JwtRequest jwtRequest) throws Exception {


        Member member = memberRepository.findByUsername(jwtRequest.getUsername())
                .orElseThrow(()-> new UsernameNotFoundException("USER_NOT_FOUND"));

        if(!passwordEncoder.matches(jwtRequest.getPassword(), member.getPassword())){
            throw new UsernameNotFoundException("INVALID_CREDENTIALS");
        }


        List<String> roles = new ArrayList<>();
        if(member.getAuthorities() != null){
            roles = member.getAuthorities().stream()
                    .map(i->i.toString())
                    .collect(Collectors.toList());
        }

        JwtResponse response = JwtResponse.builder()
                .token(jwtTokenProvider.tokenIssue(member.getUsername(), roles))
                .refreshToken(jwtTokenProvider.refreshTokenIssue(member.getUsername(), roles))
                .build();

        return new ResponseEntity(response, HttpStatus.OK);
//        return jwtTokenProvider.createToken(member.getUsername(), roles);
    }

    @PostMapping("/api/auth/token/refresh")
    public ResponseEntity<?> tokenRefresh(@RequestBody JwtRequest jwtRequest){
        Member member = memberRepository.findByUsername(jwtRequest.getUsername())
                .orElseThrow(()-> new UsernameNotFoundException("USER_NOT_FOUND"));

        try{
            jwtTokenProvider.validateToken(jwtRequest.getRefreshToken());

            List<String> roles = new ArrayList<>();
            if(member.getAuthorities() != null){
                roles = member.getAuthorities().stream()
                        .map(i->i.toString())
                        .collect(Collectors.toList());
            }

            JwtResponse response = JwtResponse.builder()
                    .token(jwtTokenProvider.tokenIssue(member.getUsername(), roles))
                    .refreshToken(jwtTokenProvider.refreshTokenIssue(member.getUsername(), roles))
                    .build();

            return new ResponseEntity(response, HttpStatus.OK);

        }catch (Exception ex){
            return new ResponseEntity(ex.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

}
