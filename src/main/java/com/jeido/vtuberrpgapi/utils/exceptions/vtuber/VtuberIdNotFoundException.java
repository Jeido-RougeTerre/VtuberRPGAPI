package com.jeido.vtuberrpgapi.utils.exceptions.vtuber;

import java.util.UUID;

public class VtuberIdNotFoundException extends IllegalArgumentException {
    public VtuberIdNotFoundException(UUID id) {
        super("Vtuber with id " + id + " not found");
    }
}
