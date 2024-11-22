package com.jeido.vtuberrpgapi.utils.exceptions.stat;

import java.util.UUID;

public class StatNotFoundException extends RuntimeException {
    public StatNotFoundException(UUID id) {
        super("Stat with Id " + id + " not found");
    }

    public StatNotFoundException(UUID vtuberId, String label) {
        super("Stat labelled '" + label + "' for Vtuber with ID " + vtuberId + " not found");
    }
}
