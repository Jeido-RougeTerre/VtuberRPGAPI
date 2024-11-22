package com.jeido.vtuberrpgapi.utils.exceptions.stat_influence;

import com.jeido.vtuberrpgapi.utils.exceptions.APIException;

import java.util.UUID;

public class StatInfluenceNotFoundException extends APIException {
    public StatInfluenceNotFoundException(UUID id) {
        super("Stat Influence with id " + id + " not found");
    }
}
