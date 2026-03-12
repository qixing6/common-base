package com.example.commonbase.jjwt;
import com.example.commonbase.exception.BusinessException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class JwtTokenUtil {

    private final SecretKey secretKey;
    private final StringRedisTemplate redisTemplate;
    private static final String BL_PREFIX = "bl:";

    public JwtTokenUtil(JwtConfig jwtConfig, StringRedisTemplate redisTemplate) {
        String secret = jwtConfig.getSecret();
        if (secret == null || secret.length() < 32) {
            throw new IllegalArgumentException("JWT密钥长度至少32位");
        }
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        this.redisTemplate = redisTemplate;
    }

    public String generate(String subject, Map<String, Object> claims, long expireMillis) {
        Date now = new Date();
        return Jwts.builder()
                .claims(claims != null ? claims : Map.of())
                .subject(subject)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + expireMillis))
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();
    }

    public Claims parse(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            if (isBlacklisted(token)) {
                throw new BusinessException("令牌作废");
            }
            return claims;
        } catch (ExpiredJwtException e) {
            throw new BusinessException("令牌已过期");
        } catch (JwtException e) {
            throw new BusinessException("令牌验证失败");
        }
    }

    public void blacklist(String token) {
        try {
            Claims claims = parse(token);
            long remain = claims.getExpiration().getTime() - System.currentTimeMillis();
            if (remain > 0) {
                redisTemplate.opsForValue().set(BL_PREFIX + token, "1", remain, TimeUnit.MILLISECONDS);
                log.debug("Token拉黑，剩余{}ms", remain);
            }
        } catch (BusinessException e) {
            log.debug("Token无需拉黑：{}", e.getMessage());
        }
    }

    private boolean isBlacklisted(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(BL_PREFIX + token));
    }
}