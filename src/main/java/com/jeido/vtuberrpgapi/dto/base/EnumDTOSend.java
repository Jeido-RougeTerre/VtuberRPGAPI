package com.jeido.vtuberrpgapi.dto.base;

import com.jeido.vtuberrpgapi.utils.enums.LicenseType;
import com.jeido.vtuberrpgapi.utils.enums.Performer;
import com.jeido.vtuberrpgapi.utils.enums.Status;
import lombok.Data;


@Data
public class EnumDTOSend {
    private final LicenseType[] licenseTypeList = LicenseType.values();
    private final Performer[] performerList = Performer.values();
    private final Status[] statusList = Status.values();
}
