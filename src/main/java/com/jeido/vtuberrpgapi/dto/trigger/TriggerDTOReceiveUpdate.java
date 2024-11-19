package com.jeido.vtuberrpgapi.dto.trigger;

import com.jeido.vtuberrpgapi.dto.stat_influence.StatInfluenceDTOSend;
import jakarta.validation.constraints.*;
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
public class TriggerDTOReceiveUpdate {

    @NotNull(message = "Label should not be Empty")
    @NotEmpty(message = "Label should not be Empty")
    @NotBlank(message = "Label should not be Empty")
    @Size(min = 3, max = 25, message = "Label should be between 3 and 25 characters")
    @Pattern(regexp = "^[a-z0-9_-]{3,25}$", message = "Label can have only English lower case letters, numbers, - and _ and be between 3 and 25 characters")
    private String label;

    private List<StatInfluenceDTOSend> influences;
}
