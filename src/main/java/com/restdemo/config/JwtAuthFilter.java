package com.restdemo.config;

import com.restdemo.service.JwtService;
import com.restdemo.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component // 이 클래스가 Spring의 컴포넌트 스캔에 의해 관리되는 빈임을 나타냅니다.
public class JwtAuthFilter extends OncePerRequestFilter { // 요청당 필터가 한번만 실행됨
	// JWT(JSON Web Token) 인증을 처리하는 필터
	// JSON 객체를 사용하여 정보를 안전하게 전달하는 방식
	/* JWT는 세 부분으로 구성됩니다:
	Header (헤더): 토큰 유형과 해싱 알고리즘 정보를 담고 있습니다.
	Payload (페이로드): 토큰에 포함될 실제 데이터(예: 사용자 정보, 권한 등)를 담고 있습니다.
	Signature (서명): 헤더와 페이로드를 조합한 후 비밀 키를 사용하여 생성된 서명으로, 토큰의 무결성과 인증을 확인하는 데 사용됩니다.
	 */
	 
    @Autowired
    private JwtService jwtService;

    @Autowired
    UserService userService;

    @Override 
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    	// JwtAuthFilter 실행 시 doFilterInternal 메서드가 최초로 호출되게 된다 (OncePerRequestFilter 오버라이딩하고 있기 때문)
    	// 해당 필터는 Security에 의해 호출되는데 doFilterInternal 메서드를 호출하도록 내부적으로 설정되어있나봄
    	
        String authHeader = request.getHeader("Authorization"); // 요청의 헤더에서 "Authorization" 헤더 값을 가져옵니다.
        String token = null;
        String username = null;
        if(authHeader != null && authHeader.startsWith("Bearer ")){ // Authorization 헤더가 null이 아니고 "Bearer "로 시작하는지 확인합니다.
            token = authHeader.substring(7); // "Bearer " 이후의 문자열을 토큰으로 사용합니다.
            username = jwtService.extractUsername(token); // 토큰에서 사용자명을 추출합니다.
        }

        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){ // 사용자명이 null이 아니고, 현재 SecurityContext에 인증 정보가 없는 경우 처리합니다.
            UserDetails userDetails = userService.loadUserByUsername(username); // 사용자 정보 로드
            if(jwtService.validateToken(token, userDetails)){ // 토큰이 유효한지 확인합니다.
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()); // 인증 토큰을 생성합니다.
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));  // 요청 세부 정보를 설정합니다.
                SecurityContextHolder.getContext().setAuthentication(authenticationToken); // SecurityContext에 인증 정보를 설정합니다.
            }

        }

        filterChain.doFilter(request, response);
    }
}
