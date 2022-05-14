package com.example.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.validation.Errors;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ErrorResponse {

    private String code;
    private String message;
    private LocalDateTime timestamp;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<Map<String, Object>> fieldErrors;

    @Builder
    private ErrorResponse(final String code, final String message, Errors errors) {
        this.code = code;
        this.message = message;
        this.timestamp = LocalDateTime.now();

        if(Objects.nonNull(errors)){
            this.fieldErrors = this.convertToFieldErrors(errors);
        }
    }

    private List<Map<String, Object>> convertToFieldErrors(Errors errors) {

        return errors
            .getFieldErrors()
            .stream()
            .map(error -> {
                Map<String, Object> fieldError = new HashMap<>();

                fieldError.put("field", error.getField());
                fieldError.put("code", error.getCode());
                fieldError.put("message", error.getDefaultMessage());
                if (error.getRejectedValue() != null) {
                    fieldError.put("rejectedValue", error.getRejectedValue());
                }

                return fieldError;
            })
            .collect(Collectors.toList());
    }
}
