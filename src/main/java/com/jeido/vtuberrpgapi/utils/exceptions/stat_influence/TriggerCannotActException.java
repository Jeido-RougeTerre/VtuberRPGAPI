package com.jeido.vtuberrpgapi.utils.exceptions.stat_influence;

import com.jeido.vtuberrpgapi.utils.exceptions.APIException;

import java.util.UUID;

public class TriggerCannotActException extends APIException {
    public TriggerCannotActException(UUID triggerId) {
        super("Trigger " + triggerId.toString() + " cannot be acted. No Influence found.");
    }
}
