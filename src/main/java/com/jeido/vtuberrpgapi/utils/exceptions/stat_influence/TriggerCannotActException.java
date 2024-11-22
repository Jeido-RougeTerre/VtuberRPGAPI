package com.jeido.vtuberrpgapi.utils.exceptions.stat_influence;

import java.util.UUID;

public class TriggerCannotActException extends IllegalArgumentException {
    public TriggerCannotActException(UUID triggerId) {
        super("Trigger " + triggerId.toString() + " cannot be acted. No Influence found.");
    }
}
