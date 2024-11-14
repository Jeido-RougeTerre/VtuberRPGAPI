package com.jeido.vtuberrpgapi.utils.exceptions.trigger;

import java.util.UUID;

public class TriggerNotFoundException extends IllegalArgumentException {
    public TriggerNotFoundException(String label, UUID vtuberId) {
        super("Trigger with label '" + label + "' not found for vtuber with ID " + vtuberId);
    }
}
