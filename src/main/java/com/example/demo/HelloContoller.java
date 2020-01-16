package com.example.demo;

import com.example.demo.api.member.Member;
import com.example.demo.api.member.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
@Slf4j
public class HelloContoller {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init(){
        Member member = Member.builder()
                        .username("wonzopein")
                        .password(passwordEncoder.encode("1"))
                        .name("오승현")
                        .build();

        memberRepository.save(member);
    }





}
