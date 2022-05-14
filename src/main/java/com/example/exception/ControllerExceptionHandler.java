package com.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {

    /**
     * 아이디 혹은 비밀번호를 잘못 입력했을 경우 발생하는 Exception을 핸들링한다.
     * @param e
     * @return
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> onBadCredentialsException(BadCredentialsException e) {

        ErrorCode errorCode = ErrorCode.LOGIN_USER_AUTHENTICATION_INVALID;

        final ErrorResponse response =
            ErrorResponse
                .builder()
                .code(errorCode.name())
                .message(errorCode.getMessage())
                .build();

        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

    /**
     * 접근권한이 없는 API를 호출했을 때 Exception을 핸들링한다.
     * @param e
     * @return
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> onAccessDeniedException(AccessDeniedException e) {

        ErrorCode errorCode = ErrorCode.LOGIN_USER_FORBIDDEN;

        final ErrorResponse response =
            ErrorResponse
                .builder()
                .code(errorCode.name())
                .message(errorCode.getMessage())
                .build();

        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }


    @ExceptionHandler(BindException.class)
    protected ResponseEntity<ErrorResponse> bindException(BindException e) {
        ErrorResponse response = ErrorResponse.builder()
            .code("Bad Request")
            .message("Bad Request")
            .errors(e)
            .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> onBusinessException(BusinessException e) {

        ErrorCode errorCode = e.getErrorCode();

        final ErrorResponse response =
            ErrorResponse
                .builder()
                .code(errorCode.name())
                .message(e.getMessage())
                .build();

        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> onException(Exception e) {

        log.error("", e);

        final ErrorResponse response =
            ErrorResponse
                .builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.name())
                .message(e.toString())
                .build();

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
