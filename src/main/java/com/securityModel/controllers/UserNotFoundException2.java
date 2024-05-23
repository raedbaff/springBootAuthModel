package com.securityModel.controllers;

public class UserNotFoundException2 extends RuntimeException {
    public UserNotFoundException2(Long userid) {
        super("user not found"+userid);
    }
}
