package com.jeido.vtuberrpgapi.utils.exceptions.user;

import java.util.UUID;

public class VtuberAlreadyAssociatedException extends IllegalArgumentException {
    public VtuberAlreadyAssociatedException(UUID userId, UUID vtuberId) {
        super(String.format("Vtuber #%s is already associated to user #%s",
                vtuberId,
                userId));
    }
}
