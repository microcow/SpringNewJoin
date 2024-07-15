package com.restdemo.controller;

import com.restdemo.domain.AuthRequestDTO;
import com.restdemo.domain.JwtResponseDTO;
import com.restdemo.domain.User;
import com.restdemo.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController // 리턴되는 정보가 json 형태로 리턴된다
public class UserController {

    @Autowired
    JwtService jwtService;
    @Autowired
    AuthenticationManager authenticationManager;

    @GetMapping("/hello")
    public User greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return new User();
    }

    @PostMapping("/api/login")
    public JwtResponseDTO AuthenticateAndGetToken(@RequestBody AuthRequestDTO authRequestDTO){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequestDTO.getUsername(), authRequestDTO.getPassword()));
        if(authentication.isAuthenticated()){
            JwtResponseDTO jwtResponseDTO = new JwtResponseDTO();
            jwtResponseDTO.setAccessToken(jwtService.GenerateToken(authRequestDTO.getUsername()));
            return jwtResponseDTO;
        } else {
            throw new UsernameNotFoundException("invalid user request..!!");
        }
    }
    
    // @PreAuthorize 어노테이션을 사용하여 특정 메서드가 실행되기 전에 접근 권한을 확인할 수 있습니다.
    // 이 메서드는 ROLE_ADMIN 권한을 가진 사용자만 호출할 수 있음
    @PreAuthorize("hasAuthority('ROLE_ADMIN')") 
    @GetMapping("/ping")
    public String test() {
        try {
            return "Welcome";
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
    
    // 이 메서드는 ROLE_USER 권한을 가진 사용자이면서, username 파라미터 값이 현재 사용자와 동일한 경우에만 호출할 수 있음
    @PreAuthorize("hasRole('ROLE_USER') and #username == authentication.principal.username")
    public void userSpecificOperation(String username) {  
    }

}
