package com.jeido.vtuberrpgapi.utils.exceptions.user;

public class UsernameNotFoundException extends IllegalArgumentException {
    public UsernameNotFoundException(String username) {
        super("User '"+username+"' not found");
    }
}
