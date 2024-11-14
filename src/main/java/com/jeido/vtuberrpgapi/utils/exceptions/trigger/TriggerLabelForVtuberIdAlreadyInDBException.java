package com.jeido.vtuberrpgapi.utils.exceptions.trigger;

import java.util.UUID;

public class TriggerLabelForVtuberIdAlreadyInDBException extends IllegalArgumentException {
    public TriggerLabelForVtuberIdAlreadyInDBException(String label, UUID vtuberId) {
        super("Trigger labeled '" + label + "' already exists for Vtuber With ID " + vtuberId);
    }
}
