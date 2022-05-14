package com.example.presentation.user.signup;

import com.example.domain.user.UserInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class SignupResponseDto {

    private String username;
    private String nickname;

    public static SignupResponseDto to(UserInfo userInfo) {
        return SignupResponseDto.builder()
            .username(userInfo.getUsername())
            .nickname(userInfo.getNickname())
            .build();
    }
}
