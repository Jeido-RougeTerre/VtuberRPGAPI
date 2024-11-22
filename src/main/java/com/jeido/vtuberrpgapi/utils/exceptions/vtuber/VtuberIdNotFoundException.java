package com.jeido.vtuberrpgapi.utils.exceptions.vtuber;

import com.jeido.vtuberrpgapi.utils.exceptions.APIException;

import java.util.UUID;

public class VtuberIdNotFoundException extends APIException {
    public VtuberIdNotFoundException(UUID id) {
        super("Vtuber with id " + id + " not found");
    }
}
