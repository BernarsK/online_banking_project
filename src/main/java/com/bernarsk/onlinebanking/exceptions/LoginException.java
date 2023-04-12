package com.bernarsk.onlinebanking.exceptions;

public class LoginException extends RuntimeException {
    public LoginException(String message) {
        super(message);
    }
    public static LoginException invalidCredentials() {
        return new LoginException("Invalid email/password combination");
    }
}
