package com.jeido.vtuberrpgapi.utils.exceptions.stat;

import java.util.UUID;

public class CanNotChangeVtuberIDException extends RuntimeException {
    public CanNotChangeVtuberIDException(UUID vtuberId, UUID oldVtuberId) {
        super("When updating a stat a VtuberId cannot change ! (cannot change vtuberId " + oldVtuberId + " to " + vtuberId + ")");
    }
}
