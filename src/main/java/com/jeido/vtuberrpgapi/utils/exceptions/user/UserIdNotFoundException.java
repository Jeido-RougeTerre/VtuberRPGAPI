package com.jeido.vtuberrpgapi.utils.exceptions.user;

import java.util.UUID;

public class UserIdNotFoundException extends IllegalArgumentException {
    public UserIdNotFoundException(UUID id) {
        super("User with id " + id + " not found");
    }
}
