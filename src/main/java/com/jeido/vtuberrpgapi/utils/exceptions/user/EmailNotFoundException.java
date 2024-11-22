package com.jeido.vtuberrpgapi.utils.exceptions.user;

import com.jeido.vtuberrpgapi.utils.exceptions.APIException;

public class EmailNotFoundException extends APIException {
    public EmailNotFoundException(String mail) {
        super("User with email '" + mail + "' not found");
    }
}
