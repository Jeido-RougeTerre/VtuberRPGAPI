package com.jeido.vtuberrpgapi.utils.exceptions.user;

import com.jeido.vtuberrpgapi.utils.exceptions.APIException;

public class InvalidLoginException extends APIException {
    public InvalidLoginException() {
        super("Invalid username/email or password");
    }
}
