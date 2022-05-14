package com.example.domain.user;

import java.util.Collections;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.user.authority.Authority;
import com.example.domain.user.authority.AuthorityType;
import com.example.exception.BusinessException;
import com.example.exception.ErrorCode;
import com.example.infrastructure.user.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserInfo signup(final String username, final String nickname, final String password) {

        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다.");
        }

        Authority authority = Authority.builder()
            .authorityName(AuthorityType.ROLE_USER)
            .build();

        User user = User.builder()
            .username(username)
            .password(passwordEncoder.encode(password))
            .nickname(nickname)
            .authorities(Collections.singleton(authority))
            .build();

        User createUser = userRepository.save(user);

        return UserInfo.of(createUser);
    }

    @Transactional(readOnly = true)
    public UserInfo getUserByUsername(String username) {

        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        return UserInfo.of(user);
    }

    @Transactional
    public void deleteUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        userRepository.delete(user);
    }
}
