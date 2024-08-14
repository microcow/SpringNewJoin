package com.restdemo.controller;

import com.restdemo.domain.AuthRequestDTO;
import com.restdemo.domain.JwtResponseDTO;
import com.restdemo.domain.User;
import com.restdemo.service.JwtService;
import com.restdemo.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController // 리턴되는 정보가 json 형태로 리턴된다 //jdk랑 그래들 버전 통일 (gredle8.9 / jdk22)
public class UserController {

 
    @Autowired
    JwtService jwtService;
    @Autowired
    UserService userservice;
    @Autowired
    PasswordEncoder passwordEncoder;
   
    @Autowired
    AuthenticationManager authenticationManager;

    @GetMapping("/hello")
    public User greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return new User();
    }

    @PostMapping("/api/SetToken")
    public JwtResponseDTO AuthenticateAndGetToken(@RequestBody AuthRequestDTO authRequestDTO){ // @RequestBody 애노테이션은 프론트에서 보낸 요청의 본문(즉, body)을 Java 객체로 변환하는 데 사용
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequestDTO.getUsername(), authRequestDTO.getPassword()));
        if(authentication.isAuthenticated()){
            JwtResponseDTO jwtResponseDTO = new JwtResponseDTO();
            jwtResponseDTO.setAccessToken(jwtService.GenerateToken(authRequestDTO.getUsername()));
            return jwtResponseDTO;
        } else {
            throw new UsernameNotFoundException("invalid user request..!!");
        }
    }
   
    @PostMapping("/api/Signup")
    public JwtResponseDTO Signup(@RequestBody User user){
    	
    	user.setPassword(passwordEncoder.encode(user.getPassword())); // BCryptPasswordEncoder를 주입받음 (BCryptPasswordEncoder 객체를 생성한 후, encode 메서드를 사용하여 비밀번호를 인코딩)
    	userservice.createUser(user); ////프론트에서 보내는 username의 값이 username에 매칭되지 않네(프론트 문제임)
    	
    	AuthRequestDTO token = new AuthRequestDTO();
    	token.setUsername(user.getUsername());
    	token.setPassword(user.getPassword());
    	
    	return AuthenticateAndGetToken(token);
    }
    @Secured({"ROLE_USER"})
    @PostMapping("/api/Signin")
    public JwtResponseDTO Signin(@RequestBody User user){ // 프론트에서 값을 전달받지 못하고있음
    	User getUser = userservice.readUser(user.getUsername());
    	
    	if (getUser.getUsername().equals(user.getUsername()) ||
    		getUser.getPassword().equals(user.getPassword())) {
    	
    	AuthRequestDTO token = new AuthRequestDTO();
    	token.setUsername(getUser.getUsername());
    	token.setPassword(getUser.getPassword());
    	
    	System.out.println("success");
    	
    	return AuthenticateAndGetToken(token);
    	}
    	else return null;
    }
   
   
    // @PreAuthorize 어노테이션을 사용하여 특정 메서드가 실행되기 전에 접근 권한을 확인할 수 있습니다.
    // 이 메서드는 ROLE_ADMIN 권한을 가진 사용자만 호출할 수 있음
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Secured({"ROLE_USER"}) // @PreAuthorize와 @Secured의 차이 : @PreAuthorize는 보다 복잡한 표현식 사용 가능
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