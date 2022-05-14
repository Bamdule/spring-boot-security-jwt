package com.example.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    LOGIN_USER_FORBIDDEN(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
    LOGIN_USER_AUTHENTICATION_INVALID(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호를 잘못 입력 했습니다."),

    JWT_NOT_AUTHENTICATION(HttpStatus.BAD_REQUEST, "인증되지 않은 토큰입니다."),
    JWT_INVALID(HttpStatus.BAD_REQUEST, "올바르지 않는 토큰입니다."),
    JWT_EXPIRATION(HttpStatus.BAD_REQUEST, "만료된 토큰입니다."),
    JWT_UNSUPPORTED(HttpStatus.BAD_REQUEST, "지원하지 않는 토큰입니다."),

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않은 회원입니다.")
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
