package com.example.spring3security6docker.service.security;

import com.example.spring3security6docker.app.ApiException;

public class AuthException extends ApiException {
    public AuthException(String message, String errorCode) {
        super(message, errorCode);
    }
}
