package com.restdemo.service;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Component // Spring에서 이 클래스를 빈으로 등록합니다.
public class JwtService { // JWT 토큰의 생성, 검증 및 클레임 추출

    public static final String SECRET = "357638792F423F4428472B4B6250655368566D597133743677397A2443264629";
    // SECRET: JWT 서명에 사용될 비밀 키입니다. Base64로 인코딩된 문자열
    
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
        // JWT 토큰에서 사용자 이름을 추출합니다. extractClaim 메서드를 사용하여 클레임의 주제(subject)를 가져옵니다.
        // 클레임(Claims)은 JWT(JSON Web Token) 내에 포함된 정보들을 말합니다.
    }
    
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
        // JWT 토큰에서 만료 날짜를 추출합니다. extractClaim 메서드를 사용하여 클레임의 만료 날짜를 가져옵니다.
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
        // 주어진 JWT 토큰에서 특정 클레임을 추출합니다. claimsResolver 함수는 클레임을 처리하여 원하는 값을 반환합니다.
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        // JWT 토큰에서 모든 클레임을 추출합니다. parserBuilder를 사용하여 토큰을 파싱하고, getBody를 통해 클레임의 본문을 가져옵니다.
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
        // JWT 토큰이 만료되었는지 확인합니다. 만료 날짜가 현재 날짜보다 이전인지 검사합니다.
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        // JWT 토큰이 유효한지 검사합니다. 사용자 이름이 일치하고, 토큰이 만료되지 않았는지 확인합니다.
    }



    public String GenerateToken(String username){
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
        // GenerateToken: 사용자 이름을 기반으로 JWT 토큰을 생성합니다. 빈 클레임 맵을 사용하여 createToken 메서드를 호출합니다.
    }



    private String createToken(Map<String, Object> claims, String username) {

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*10))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
        // JWT 토큰을 생성합니다. 클레임, 주제, 발행일, 만료일 및 서명을 설정합니다. 만료일은 현재 시간으로부터 10분 후로 설정합니다.
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
        // 비밀 키를 생성합니다. SECRET을 Base64로 디코딩하여 HMAC-SHA 키를 생성합니다. 이 키는 토큰 서명에 사용됩니다.
    }
}
