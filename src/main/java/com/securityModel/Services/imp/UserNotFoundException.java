package com.securityModel.Services.imp;

public class UserNotFoundException extends RuntimeException  {
    public UserNotFoundException(String sender) {
        super("User not found with id: " + sender);
    }


}
