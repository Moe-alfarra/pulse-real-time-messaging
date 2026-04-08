package com.moeAlfarra.real_time_messaging_platform.dto;

public class AuthResponse {

    private String token;


    public AuthResponse() {

    }
    public AuthResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
