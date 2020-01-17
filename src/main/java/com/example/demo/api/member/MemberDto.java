package com.example.demo.api.member;


import lombok.Data;
import lombok.NoArgsConstructor;

public class MemberDto {

    @NoArgsConstructor
    @Data
    static class Join {
        private String username;
        private String password;
        private String name;
    }

    @NoArgsConstructor
    @Data
    static class Get {
        private String username;
        private String name;
    }

    @NoArgsConstructor
    @Data
    static class Put {
        private String username;
        private String name;
    }

}
