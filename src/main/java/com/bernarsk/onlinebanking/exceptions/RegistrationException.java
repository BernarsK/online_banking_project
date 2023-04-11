package com.bernarsk.onlinebanking.exceptions;

public class RegistrationException extends RuntimeException {
    public RegistrationException(String message) {
        super(message);
    }
    public static RegistrationException emailNotAvailable() {
        return new RegistrationException("Email not available");
    }
}
