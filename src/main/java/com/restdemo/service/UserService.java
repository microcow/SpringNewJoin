package com.restdemo.service;

import com.restdemo.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Collection;

public interface UserService extends UserDetailsService {
	/* UserService는 UserDetailService를 '상속(extends)'받고 있기에
	UserService를 '구현(implements)'하는 클래스는 UserDetailsService의 메서드인 loadUserByUsername 메서드와
	UserService의 메서드를 모두 오버라이딩 해주어야 한다.
	*/
	//
    //유저읽기
    public User readUser(String username);

    //유저생성
    public void createUser(User user);

    // 권한 생성
    public void createAuthorities(User user);

    // 시큐리티 권한 얻기
    Collection<GrantedAuthority> getAuthorities(String username);

}
