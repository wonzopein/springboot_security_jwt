package com.example.demo.member;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/members")
public class MemberApi {

    @Autowired
    MemberRepository memberRepository;

    @Autowired ModelMapper modelMapper;
    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("")
    public MemberDto.Get createMember(@RequestBody MemberDto.Join memberJoin){
        //  멤버 저장
        Member memberEnity = modelMapper.map(memberJoin, Member.class);
        memberEnity.setPassword(passwordEncoder.encode(memberEnity.getPassword()));
        memberEnity = memberRepository.save(memberEnity);
        //  멤버 변환
        MemberDto.Get member = modelMapper.map(memberEnity, MemberDto.Get.class);
        return member;
    }

    @GetMapping("")
    public List<MemberDto.Get> getAllMembers(){

        List<Member> memberRepositoryAll = memberRepository.findAll();
        List<MemberDto.Get> members = memberRepositoryAll.stream()
                                            .map(member->modelMapper.map(member, MemberDto.Get.class))
                                            .collect(Collectors.toList());
        return members;
    }

    @PutMapping("")
    public MemberDto.Get putMember(@RequestBody MemberDto.Put memberPut){

        //  멤버 조회
        Member memberEntity = memberRepository.findByUsername(memberPut.getUsername()).orElseThrow(() -> new UsernameNotFoundException(""));

        memberEntity = modelMapper.map(memberPut, Member.class);
        memberEntity = memberRepository.save(memberEntity);
        MemberDto.Get member = modelMapper.map(memberEntity, MemberDto.Get.class);

        return member;
    }

    @GetMapping("/{username}")
    public MemberDto.Get getMember(@PathVariable("username") String username){
        //  멤버 조회
        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(""));
        //  멤버 조회형태로 변환
        MemberDto.Get memberGet = modelMapper.map(member, MemberDto.Get.class);
        return memberGet;
    }



}
