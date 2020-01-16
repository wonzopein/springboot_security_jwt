package com.example.demo.api.member;


import lombok.Data;
import lombok.NoArgsConstructor;

public class MemberDto {

    @NoArgsConstructor
    @Data
    static class Join {
        String username;
        String password;
        String name;
    }

    @NoArgsConstructor
    @Data
    static class Get {
        String username;
        String name;
    }

    @NoArgsConstructor
    @Data
    static class Put {
        String username;
        String name;
    }

}
