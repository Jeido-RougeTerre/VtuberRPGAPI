package com.jeido.vtuberrpgapi.utils.exceptions.stat_influence;

import com.jeido.vtuberrpgapi.utils.exceptions.APIException;

import java.util.UUID;

public class NotMatchingVtuberIdException extends APIException {
    public NotMatchingVtuberIdException(UUID triggerId, UUID statId) {
        super("Not matching Vtuber while creating StatInfluence :\nTrigger ID: " + triggerId + "\nStat ID: " + statId);
    }
}
