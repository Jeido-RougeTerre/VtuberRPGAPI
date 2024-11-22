package com.jeido.vtuberrpgapi.utils.exceptions.user;

import com.jeido.vtuberrpgapi.utils.exceptions.APIException;

public class UsernameNotFoundException extends APIException {
    public UsernameNotFoundException(String username) {
        super("User '"+username+"' not found");
    }
}
