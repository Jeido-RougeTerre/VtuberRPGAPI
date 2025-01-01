package com.jeido.vtuberrpgapi.dto.vtuber;

import com.jeido.vtuberrpgapi.dto.stat.StatDTOSendLess;
import com.jeido.vtuberrpgapi.dto.trigger.TriggerDTOSendLess;
import com.jeido.vtuberrpgapi.utils.enums.LicenseType;
import com.jeido.vtuberrpgapi.utils.enums.Performer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VtuberDTOSend {
    private UUID id;

    private String title;
    private String thumbnailPath;
    private String version;
    private String author;
    private String contact;
    private String reference;
    private Performer performer;
    private boolean allowViolent;
    private boolean allowSexual;
    private boolean allowCommercial;
    private String otherLicenseUrl;
    private LicenseType redistribution;
    private String otherRedistributionUrl;

    private List<UUID> userIds;
    private List<StatDTOSendLess> stats;
    private List<TriggerDTOSendLess> triggers;
}
