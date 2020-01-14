package com.example.demo;

import com.example.demo.member.Member;
import com.example.demo.member.MemberRepository;
import com.example.demo.properties.JwtAuthProperties;
import com.example.demo.security.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.security.auth.login.CredentialException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
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

    @GetMapping("/api/auth/signin")
    public String singin(@RequestParam String username, @RequestParam String password) throws Exception {

        Member member = memberRepository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException(""));

        if(!passwordEncoder.matches(password, member.getPassword())){
            return "error";
        }

        List<String> roles = new ArrayList<>();
        if(member.getAuthorities() != null){
            roles = member.getAuthorities().stream()
                    .map(i->i.toString())
                    .collect(Collectors.toList());
        }


        return jwtTokenProvider.createToken(member.getUsername(), roles);
    }

}
