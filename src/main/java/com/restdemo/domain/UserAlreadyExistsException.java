package com.restdemo.domain;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
    
    public UserAlreadyExistsException(String message, Throwable cause) { // Throwable은 Java의 예외 및 오류 처리를 위한 최상위 클래스
        super(message, cause);
    }
}
