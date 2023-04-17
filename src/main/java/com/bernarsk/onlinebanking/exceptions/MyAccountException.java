package com.bernarsk.onlinebanking.exceptions;

public class MyAccountException extends RuntimeException{
    public MyAccountException(String message) {
        super(message);
    }

    public static MyAccountException userNotFoundException() {
        return new MyAccountException("User not found");
    }

    public static MyAccountException accessError() {
        return new MyAccountException("LogedInUser has no access to this function");
    }

    public static MyAccountException passwordError() {
        return new MyAccountException("Wrong old password");
    }
    public static MyAccountException emailAlreadyUsed() {
        return new MyAccountException("Email already connected to different account");
    }
}
