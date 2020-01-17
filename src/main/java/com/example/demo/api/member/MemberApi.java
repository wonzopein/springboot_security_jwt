package com.example.demo.api.member;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.function.Function;

@RestController
@RequestMapping("/api/members")
public class MemberApi {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    /**
     * 멤버 생성
     * @param memberJoin
     * @return
     */
    @PostMapping("")
    public ResponseEntity createMember(@RequestBody MemberDto.Join memberJoin){
        //  멤버 저장
        Member memberEnity = modelMapper.map(memberJoin, Member.class);
        memberEnity.setPassword(passwordEncoder.encode(memberEnity.getPassword()));
        memberEnity = memberRepository.save(memberEnity);
        //  멤버 변환
        MemberDto.Get member = modelMapper.map(memberEnity, MemberDto.Get.class);

        return new ResponseEntity(member, HttpStatus.CREATED);
    }

    /**
     * 멤버 정보 조회
     * @param username
     * @return
     */
    @GetMapping("/{username}")
    public ResponseEntity getMember(@PathVariable("username") String username){
        //  멤버 조회
        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(""));
        //  멤버 조회형태로 변환
        MemberDto.Get memberGet = modelMapper.map(member, MemberDto.Get.class);
        return new ResponseEntity(memberGet, HttpStatus.OK);
    }

    /**
     * 멤버 목록 조회
     * @return
     */
    @GetMapping("")
    public Page<MemberDto.Get> getAllMembers(Pageable pageable){

        Page<Member> memberEntity = memberRepository.findAll(pageable);
        Page<MemberDto.Get> members = memberEntity.map(new Function<Member, MemberDto.Get>() {
            @Override
            public MemberDto.Get apply(Member member) {
                return modelMapper.map(member, MemberDto.Get.class);
            }
        });
        return members;
    }

    /**
     * 멤버 정보변경
     * @param memberPut
     * @return
     */
    @PatchMapping("")
    public MemberDto.Get putMember(@RequestBody MemberDto.Put memberPut){

        //  멤버 조회
        Member memberEntity = memberRepository.findByUsername(memberPut.getUsername()).orElseThrow(() -> new UsernameNotFoundException(""));

        memberEntity = modelMapper.map(memberPut, Member.class);
        memberEntity = memberRepository.save(memberEntity);
        MemberDto.Get member = modelMapper.map(memberEntity, MemberDto.Get.class);

        return member;
    }

    /**
     * 멤버 삭제
     * @param username
     */
    @DeleteMapping("/{username}")
    public void deleteMember(@PathVariable("username") String username){
        Member member = memberRepository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException(""));
        memberRepository.delete(member);
    }





}
