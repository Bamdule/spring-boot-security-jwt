package com.example.presentation.user;

import java.time.LocalDateTime;

import com.example.domain.user.UserInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class UserResponseDto {

    private String username;
    private String nickname;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    public static UserResponseDto of(UserInfo userInfo) {
        return UserResponseDto.builder()
            .username(userInfo.getUsername())
            .nickname(userInfo.getNickname())
            .createDate(userInfo.getCreateDate())
            .updateDate(userInfo.getUpdateDate())
            .build();
    }
}
