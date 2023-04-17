package com.bernarsk.onlinebanking.exceptions;

public class RegistrationException extends RuntimeException {
    public RegistrationException(String message) {
        super(message);
    }
    public static RegistrationException emailNotAvailable() {
        return new RegistrationException("Email already registered");
    }
    public static RegistrationException passwordTooShortException() {
        return new RegistrationException("Pasword must be atleast 8 characters long");
    }

    public static RegistrationException passwordNotStrongEnoughException() {
        return new RegistrationException("Password must contain at least one uppercase letter, one lowercase letter, and one digit");
    }
}
