package com.jeido.vtuberrpgapi.utils.exceptions.stat_influence;

import java.util.UUID;

public class NotMatchingVtuberIdException extends IllegalArgumentException {
    public NotMatchingVtuberIdException(UUID triggerId, UUID statId) {
        super("Not matching Vtuber while creating StatInfluence :\nTrigger ID: " + triggerId + "\nStat ID: " + statId);
    }
}
