package com.example.domain.user;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class UserInfo {

    private String username;
    private String nickname;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    public static UserInfo of(User user) {

        return UserInfo.builder()
            .username(user.getUsername())
            .nickname(user.getNickname())
            .createDate(user.getCreateDate())
            .updateDate(user.getUpdateDate())
            .build();

    }
}
