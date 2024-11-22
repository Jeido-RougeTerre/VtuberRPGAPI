package com.jeido.vtuberrpgapi.utils.exceptions.stat;

import com.jeido.vtuberrpgapi.utils.exceptions.APIException;

import java.util.UUID;

public class StatLabelForVtuberIdAlreadyInDBException extends APIException {
    public StatLabelForVtuberIdAlreadyInDBException(String label, UUID vtuberId) {
        super("Stat labeled '" + label + "' already exists for Vtuber with ID " + vtuberId);
    }
}
