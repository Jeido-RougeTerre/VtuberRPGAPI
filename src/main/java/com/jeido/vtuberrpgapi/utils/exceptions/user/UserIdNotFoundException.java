package com.jeido.vtuberrpgapi.utils.exceptions.user;

import com.jeido.vtuberrpgapi.utils.exceptions.APIException;

import java.util.UUID;

public class UserIdNotFoundException extends APIException {
    public UserIdNotFoundException(UUID id) {
        super("User with id " + id + " not found");
    }
}
