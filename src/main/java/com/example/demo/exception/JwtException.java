package com.example.demo.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtException extends RuntimeException {

    String message;
    HttpStatus status;
    Exception exception;

    public JwtException(Exception exception) {
        this.message = exception.getMessage();
        this.status = HttpStatus.FORBIDDEN;
        this.exception = exception;
    }
}
