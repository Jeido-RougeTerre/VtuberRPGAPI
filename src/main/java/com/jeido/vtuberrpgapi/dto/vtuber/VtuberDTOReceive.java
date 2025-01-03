package com.jeido.vtuberrpgapi.dto.vtuber;

import com.jeido.vtuberrpgapi.dto.stat.StatDTOReceiveFull;
import com.jeido.vtuberrpgapi.dto.trigger.TriggerDTOReceiveCreation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String name;

    private List<UUID> userIds;
    private List<StatDTOReceiveFull> stats;
    private List<TriggerDTOReceiveCreation> triggers;
}
