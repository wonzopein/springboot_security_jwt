package com.example.demo;

import com.example.demo.api.member.Member;
import com.example.demo.api.member.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@RestController
@Slf4j
public class HelloContoller {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init(){

        List<Member> members = new ArrayList<>();
        IntStream.range(0, 30).forEach(i->{
            members.add(Member.builder().username(i+"wonzopein").password(passwordEncoder.encode("1")).name("오승현" + i).build());
        });

        memberRepository.saveAll(members);
    }





}
