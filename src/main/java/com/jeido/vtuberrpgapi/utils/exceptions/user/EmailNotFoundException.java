package com.jeido.vtuberrpgapi.utils.exceptions.user;

public class EmailNotFoundException extends IllegalArgumentException {
    public EmailNotFoundException(String mail) {
        super("User with email '" + mail + "' not found");
    }
}
