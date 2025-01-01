package com.jeido.vtuberrpgapi.dto.vtuber;

import com.jeido.vtuberrpgapi.dto.stat.StatDTOReceiveFull;
import com.jeido.vtuberrpgapi.dto.trigger.TriggerDTOReceiveCreation;
import com.jeido.vtuberrpgapi.utils.enums.LicenseType;
import com.jeido.vtuberrpgapi.utils.enums.Performer;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VtuberDTOReceive {
    @NotNull
    @NotEmpty
    @NotBlank
    private String title;
    private String thumbnailPath = "no-icon.png";
    private String version = "unknown";
    private String author;
    private String contact = "";
    private String reference = "";
    private Performer performer = Performer.ONLY_AUTHOR;
    private boolean allowViolent = false;
    private boolean allowSexual = false;
    private boolean allowCommercial = false;
    private String otherLicenseUrl = "";
    private LicenseType redistribution = LicenseType.REDISTRIBUTION_PROHIBITED;
    private String otherRedistributionUrl = "";


    private List<UUID> userIds = new ArrayList<>();
    private List<StatDTOReceiveFull> stats = new ArrayList<>();
    private List<TriggerDTOReceiveCreation> triggers = new ArrayList<>();
}
