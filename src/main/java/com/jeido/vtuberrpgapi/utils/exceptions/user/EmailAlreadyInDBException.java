package com.jeido.vtuberrpgapi.utils.exceptions.user;

import com.jeido.vtuberrpgapi.utils.exceptions.APIException;

public class EmailAlreadyInDBException extends APIException {
    public EmailAlreadyInDBException(String email) {
        super("Email '" + email + "' is already in use");
    }
}
