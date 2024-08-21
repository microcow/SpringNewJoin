package com.restdemo.domain;


public class JwtResponseDTO {
	
    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    /* 지금은 accessToken 인스턴스 변수를 사용하고 있어서 AuthenticateAndGetToken 메서드에서 토큰을 생성할 때 setAccessToken을 사용하고 있고, 프론트측에서도 토큰을 가져올 때 reponsData.accessToken 이런 식으로 가져오고 있지만
     * 서버 측에서 responseData.jwt 이런 식으로 토큰을 가져오고 싶다면 AuthenticateAndGetToken 메서드에서 토큰을 생성할 때 setJwt를 사용해서 jwt라는 인스턴스 변수에 토큰을 저장해야함
    private String jwt;

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }
    */
}