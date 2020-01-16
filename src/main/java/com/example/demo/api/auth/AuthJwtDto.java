package com.example.demo.api.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class AuthJwtDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    static class Request {
        String username;
        String password;

        String refreshToken;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    static class Response {
        String token;
        String refreshToken;
    }

}
