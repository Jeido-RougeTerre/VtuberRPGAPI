package com.jeido.vtuberrpgapi.dto.base;

import com.jeido.vtuberrpgapi.utils.enums.Status;
import lombok.Data;

@Data
public class BaseDTOSend {
    private String version = "1.0.0";
    private Status status = Status.OK;
    private String message = "Vtuber RPG API is working !\n☆*: .｡. o(≧▽≦)o .｡.:*☆";
}
