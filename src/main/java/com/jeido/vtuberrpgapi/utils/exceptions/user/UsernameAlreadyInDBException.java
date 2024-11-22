package com.jeido.vtuberrpgapi.utils.exceptions.user;

import com.jeido.vtuberrpgapi.utils.exceptions.APIException;

public class UsernameAlreadyInDBException extends APIException {
    public UsernameAlreadyInDBException(String username) {
        super("Username '" + username + "' is already in use!");
    }
}
