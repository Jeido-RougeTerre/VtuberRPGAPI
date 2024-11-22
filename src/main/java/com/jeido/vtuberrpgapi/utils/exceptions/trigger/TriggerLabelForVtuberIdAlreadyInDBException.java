package com.jeido.vtuberrpgapi.utils.exceptions.trigger;

import com.jeido.vtuberrpgapi.utils.exceptions.APIException;

import java.util.UUID;

public class TriggerLabelForVtuberIdAlreadyInDBException extends APIException {
    public TriggerLabelForVtuberIdAlreadyInDBException(String label, UUID vtuberId) {
        super("Trigger labeled '" + label + "' already exists for Vtuber With ID " + vtuberId);
    }
}
