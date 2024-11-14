package com.jeido.vtuberrpgapi.utils.exceptions.stat;

import java.util.UUID;

public class StatLabelForVtuberIdAlreadyInDBException extends IllegalArgumentException {
    public StatLabelForVtuberIdAlreadyInDBException(String label, UUID vtuberId) {
        super("Stat labeled '" + label + "' already exists for Vtuber with ID " + vtuberId);
    }
}
