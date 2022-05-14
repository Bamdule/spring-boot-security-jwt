package com.example.domain.login;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.domain.user.User;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class LoginUser implements UserDetails {

    private Long id;
    private String username;
    private String nickname;
    private List<GrantedAuthority> grantedAuthorities;
    private boolean activated;

    private String password;

    public static LoginUser createByUser(User user) {
        List<GrantedAuthority> authorities = user.getAuthorities().stream()
            .map(authority -> new SimpleGrantedAuthority(authority.getAuthorityName().name()))
            .collect(Collectors.toList());

        return LoginUser
            .builder()
            .id(user.getId())
            .password(user.getPassword())
            .username(user.getUsername())
            .nickname(user.getNickname())
            .grantedAuthorities(authorities)
            .activated(user.isActivated())
            .build();
    }

    public static LoginUser createByClaims(Map<String, Object> claims) {
        List<GrantedAuthority> authorities = Arrays.stream(((String)claims.get("authorities")).split(","))
            .map(authority -> new SimpleGrantedAuthority(authority))
            .collect(Collectors.toList());

        return LoginUser
            .builder()
            .id(Long.valueOf((Integer)claims.get("id")))
            .username((String)claims.get("username"))
            .nickname((String)claims.get("nickname"))
            .grantedAuthorities(authorities)
            .activated(true)
            .build();
    }

    public Map<String, Object> convertToClaims() {
        String authorities = grantedAuthorities.stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","));

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", this.id);
        claims.put("username", this.username);
        claims.put("nickname", this.nickname);
        claims.put("authorities", authorities);

        return claims;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.grantedAuthorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.activated;
    }
}
