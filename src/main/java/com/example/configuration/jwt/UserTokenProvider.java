package com.example.configuration.jwt;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.domain.login.LoginUser;
import com.example.exception.BusinessException;
import com.example.exception.ErrorCode;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class UserTokenProvider implements InitializingBean {

    private static final String USER_KEY = "user";

    private final String secret;
    private final long tokenValidityInMilliseconds;

    private Key key;

    public UserTokenProvider(
        @Value("${jwt.secret}") String secret,
        @Value("${jwt.token-validity-in-seconds}") long tokenValidityInSeconds) {
        this.secret = secret;
        this.tokenValidityInMilliseconds = tokenValidityInSeconds * 1000;
    }

    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createToken(LoginUser loginUser) {

        long now = (new Date()).getTime();
        Date validity = new Date(now + this.tokenValidityInMilliseconds);

        return Jwts.builder()
            .setSubject(loginUser.getUsername())
            .claim(USER_KEY, loginUser.convertToClaims())
            .signWith(key, SignatureAlgorithm.HS512)
            .setExpiration(validity)
            .compact();
    }

    private Claims createClaimsByJwt(String jwt) {
        return Jwts
            .parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(jwt)
            .getBody();
    }

    /**
     * jwt를 인증 및 인증 정보를 생성한다.
     * @param jwt
     * @return
     */
    public Optional<LoginUser> verifyJWT(String jwt) {
        if (Strings.isBlank(jwt)) {
            return Optional.empty();
        }

        try {
            Claims claims = createClaimsByJwt(jwt);
            LoginUser loginUser = LoginUser.createByClaims((Map<String, Object>)claims.get(USER_KEY));

            return Optional.of(loginUser);

        } catch (SecurityException | MalformedJwtException e) {
            throw new BusinessException(ErrorCode.JWT_NOT_AUTHENTICATION);
        } catch (ExpiredJwtException e) {
            throw new BusinessException(ErrorCode.JWT_EXPIRATION);
        } catch (UnsupportedJwtException e) {
            throw new BusinessException(ErrorCode.JWT_UNSUPPORTED);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.JWT_INVALID);
        }
    }
}
