package com.jeido.vtuberrpgapi.utils.exceptions.user;

public class EmailAlreadyInDBException extends IllegalArgumentException {
    public EmailAlreadyInDBException(String email) {
        super("Email '" + email + "' is already in use");
    }
}
