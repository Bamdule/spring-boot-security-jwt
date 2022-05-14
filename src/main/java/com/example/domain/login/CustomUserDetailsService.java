package com.example.domain.login;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.user.User;
import com.example.infrastructure.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service("userDetailsService")
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Transactional
    @Override
    public LoginUser loadUserByUsername(final String username) {
        // return userRepository.findOneWithAuthoritiesByUsername(username)
        //     .map(user -> createUser(user))
        //     .orElseThrow(() -> new UsernameNotFoundException(username));

        User user = userRepository.findOneWithAuthoritiesByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException(username));

        return LoginUser.createByUser(user);
    }

    private org.springframework.security.core.userdetails.User createUser(User user) {

        if (!user.isActivated()) {
            throw new RuntimeException("비활성화 된 유저입니다.");
        }
        List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
            .map(authority -> new SimpleGrantedAuthority(authority.getAuthorityName().name()))
            .collect(Collectors.toList());



        return new org.springframework.security.core.userdetails.User(user.getUsername(),
            user.getPassword(),
            grantedAuthorities);
    }
}