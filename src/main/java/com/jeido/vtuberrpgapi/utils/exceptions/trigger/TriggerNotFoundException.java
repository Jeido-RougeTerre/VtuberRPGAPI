package com.jeido.vtuberrpgapi.utils.exceptions.trigger;

import java.util.UUID;

public class TriggerNotFoundException extends IllegalArgumentException {
    public TriggerNotFoundException(UUID id) {
        super("Trigger with ID " + id + " not found");
    }

    public TriggerNotFoundException(UUID vtuberId, String label) {
        super("Trigger labelled '" + label + "' for Vtuber with ID " + vtuberId + " not found");
    }
}
