package com.restdemo.service;

import com.restdemo.domain.User;
import com.restdemo.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    /*@Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        logger.debug("Entering in loadUserByUsername Method...");
        UserInfo user = userRepository.findByUsername(username);
        if(user == null){
            logger.error("Username not found: " + username);
            throw new UsernameNotFoundException("could not found user..!!");
        }
        logger.info("User Authenticated Successfully..!!!");
        return new CustomUserDetails(user);
    }*/

    @Autowired
    UserMapper userMapper;

    @Override //UserDetailsService 인터페이스는 단 하나의 메서드인 loadUserByUsername을 정의하고 있습니다. 
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.readUser(username);
        user.setAuthorities(getAuthorities(username));

        System.out.println("loadUserByUsername");

        return user;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities(String username) {
        List<GrantedAuthority> authorities = userMapper.readAuthorities(username);
        return authorities;
    }

    @Override
    public void createUser(User user) {
        userMapper.createUser(user);
    }

    @Override
    public void createAuthorities(User user) {
        userMapper.createAuthority(user);
    }

    @Override
    public User readUser(String username) {
        return userMapper.readUser(username);
    }
}
