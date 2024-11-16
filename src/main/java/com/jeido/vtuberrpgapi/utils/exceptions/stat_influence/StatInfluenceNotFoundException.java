package com.jeido.vtuberrpgapi.utils.exceptions.stat_influence;

import java.util.UUID;

public class StatInfluenceNotFoundException extends RuntimeException {
    public StatInfluenceNotFoundException(UUID id) {
        super("Stat Influence with id " + id + " not found");
    }
}
