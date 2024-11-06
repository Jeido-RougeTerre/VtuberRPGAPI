package com.jeido.vtuberrpgapi.utils.exceptions.user;

public class UsernameAlreadyInDBException extends IllegalArgumentException {
    public UsernameAlreadyInDBException(String username) {
        super("Username '" + username + "' is already in use!");
    }
}
