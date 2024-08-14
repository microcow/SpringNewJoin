package com.restdemo.config;

import com.restdemo.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

// 이 클래스는 Spring Security 설정을 담당하는 클래스임을 나타냅니다.
@Configuration
@EnableWebSecurity
@EnableMethodSecurity 
public class SecurityConfig {

    @Autowired
    JwtAuthFilter jwtAuthFilter;

    @Bean // ★ 사용자가 화면을 클릭하거나 요청을 보낼경우 Controller에 도달하기 전에 Spring Security의 필터 체인를 먼저 거친다 (내부적으로 그렇게 설정되어있음)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable) // CSRF 보호를 비활성화
                .authorizeHttpRequests((req) -> req // 요청에 대한 인가 규칙을 설정
                        .requestMatchers(new AntPathRequestMatcher("/api/Signin")).permitAll() // /api/login은 모든 사용자에게 접근을 허용 (new AntPathRequestMatcher 생략 가능)
                        .requestMatchers(new AntPathRequestMatcher("/api/Signup")).permitAll()
                        .requestMatchers("/api/**").authenticated() // /api/**는 인증된 사용자에게만 접근을 허용 (new AntPathRequestMatcher 생략)
                        .anyRequest().permitAll() // 나머지 요청은 모든 사용자에게 허용
                )
                .sessionManagement((sessionManagement) -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) 
                        // 세션 관리 정책을 STATELESS로 설정
                        // STATLESS : 서버가 세션을 관리하지 않도록 함
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class).build();
        		// jwtAuthFilter를 UsernamePasswordAuthenticationFilter 앞에 실행하도록 추가.
        		// 이 메서드는 필터의 실행 순서를 제어하고, 특정 필터를 필터 체인의 특정 위치에 추가하기 위해 사용됩니다.
        		// 즉, 새 요청이 오면 controller를 타기 전에 만들어 둔 jwtAuthFilter 클래스로 먼저 이동하게 된다.
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    } // passwordEncoder() 메서드는 BCryptPasswordEncoder를 빈으로 등록하여 비밀번호를 암호화하는 데 사용

    @Bean
    public AuthenticationManager authenticationManager( // 로그인 시 입력 값이 등록되어있는지(회원가입 되었는지) 확인하는 메서드
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        // DaoAuthenticationProvider를 사용하여 사용자 인증을 처리 (이 과정에서 loadUserByUsername이 호출되는 듯)
        // DaoAuthenticationProvider : DB에 접근하여 입력된 값을 확인하는 메서드 (만약 메모리에 접근하여 확인하고 싶을 경우 DaoAuthenticationProvider가 아닌 InMemoryAuthenticationProvider클래스를 사용해야함)
        authenticationProvider.setUserDetailsService(userDetailsService); // 유저 정보 세팅
        authenticationProvider.setPasswordEncoder(passwordEncoder); // 유저 비밀번호 세팅

        return new ProviderManager(authenticationProvider); // 세팅된 값과 유저가 입력한 값이 일치하는지 확인
    }
}
