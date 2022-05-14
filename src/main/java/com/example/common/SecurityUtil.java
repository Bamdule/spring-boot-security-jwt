package com.example.common;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.example.domain.login.LoginUser;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityUtil {

    public static LoginUser getLoginUser() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (LoginUser)authentication.getPrincipal();
    }
}
