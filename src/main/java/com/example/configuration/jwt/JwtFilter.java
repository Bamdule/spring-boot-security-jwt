package com.example.configuration.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

public class JwtFilter extends GenericFilterBean {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String JWT_PREFIX = "Bearer ";

    private UserTokenProvider tokenProvider;

    public JwtFilter(UserTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
        throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest)servletRequest;

        /*
        Http Header에서 token을 조회한다.
         */
        String jwt = this.getTokenByHeader(httpServletRequest);

        /*
        토큰이 유효하면 인증 정보를 생성 후 SecurityContextHolder 에 초기화한다.
         */
        tokenProvider.verifyJWT(jwt)
            .ifPresent(loginUser -> {
                UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(loginUser, jwt, loginUser.getAuthorities());

                this.initAuthentication(authentication);
            });

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private String getTokenByHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (Strings.isBlank(bearerToken)) {
            return null;
        }

        return bearerToken.substring(JWT_PREFIX.length());
    }

    private void initAuthentication(Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}