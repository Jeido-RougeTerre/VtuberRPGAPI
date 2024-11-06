package com.jeido.vtuberrpgapi.utils.exceptions.user;

public class InvalidLoginException extends IllegalArgumentException {
    public InvalidLoginException() {
        super("Invalid username/email or password");
    }
}
