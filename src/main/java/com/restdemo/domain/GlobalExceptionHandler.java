package com.restdemo.domain;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice // @RestControllerAdvice는 Spring의 어노테이션으로, 전역 예외 처리기를 정의하기 위해 사용됩니다. 이 어노테이션을 사용하면, 이 클래스에서 정의된 예외 처리 메서드들이 모든 컨트롤러에 적용됩니다.
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    //@ExceptionHandler는 특정 예외를 처리하는 메서드임을 나타내는 어노테이션입니다.
    //UserAlreadyExistsException.class는 처리할 예외의 타입을 지정합니다.
    //이 경우, UserAlreadyExistsException이 발생할 때 이 메서드가 호출됩니다.
    public ResponseEntity<Map<String, String>> handleUserAlreadyExistsException(UserAlreadyExistsException e) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", e.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        // ★ ResponseEntity<Map<String, String>>를 사용하여 예외 처리 메서드에서 반환하는 객체는 JSON 형식으로 자동 변환됩니다. 이 과정은 Spring Framework의 @RestControllerAdvice와 @ResponseBody 메커니즘 덕분에 이루어집니다.
        // Spring MVC는 ResponseEntity와 함께 Map을 반환하면, 이를 자동으로 JSON 형식으로 변환합니다
        // 헤더의 값 중 Content-Type 속성을 application/json으로 설정해준다
    }
}