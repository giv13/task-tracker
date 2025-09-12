package ru.giv13.tasktracker.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.giv13.tasktracker.user.User;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Arrays;
import java.util.Date;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final HttpServletResponse httpServletResponse;
    @Value("${security.jwt.secret}")
    private String jwtSecret;
    @Value("${security.jwt.expiration}")
    private Duration jwtExpiration;
    @Value("${security.jwt.token-name}")
    private String jwtTokenName;
    @Value("${security.jwt.refresh.expiration}")
    private Duration jwtRefreshExpiration;
    @Value("${security.jwt.refresh.token-name}")
    private String jwtRefreshTokenName;

    public String generateCookie(User user) {
        generateCookie(user, jwtTokenName, jwtExpiration);
        return generateCookie(user, jwtRefreshTokenName, jwtRefreshExpiration);
    }

    private String generateCookie(User user, String tokenName, Duration tokenExpiration) {
        String token = generateToken(user, tokenExpiration);
        Cookie cookie = new Cookie(tokenName, token);
        cookie.setMaxAge((int) tokenExpiration.toSeconds());
        //cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        httpServletResponse.addCookie(cookie);
        return token;
    }

    public void eraseCookie() {
        eraseCookie(jwtTokenName);
        eraseCookie(jwtRefreshTokenName);
    }

    private void eraseCookie(String tokenName) {
        Cookie refreshCookie = new Cookie(tokenName, "");
        refreshCookie.setMaxAge(0);
        refreshCookie.setPath("/");
        httpServletResponse.addCookie(refreshCookie);
    }

    public String getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;
        return Arrays.stream(cookies).filter(c -> c.getName().equals(name)).findFirst().map(Cookie::getValue).orElse(null);
    }

    private String generateToken(User user, Duration expiration) {
        return Jwts
                .builder()
                .subject(user.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration.toMillis()))
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, User user) {
        return extractUsername(token).equals(user.getUsername()) && user.getRefresh() != null && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractClaims(String token) {
        return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
