package com.jeido.vtuberrpgapi.utils.exceptions.stat;

import java.util.UUID;

public class StatNotFoundException extends RuntimeException {
    public StatNotFoundException(String label, UUID vtuberId) {
        super("Stat with label '" + label + "' not found for vtuber with Id " + vtuberId);
    }
}
