package com.jeido.vtuberrpgapi.dto.stat_influence;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatInfluenceDTOReceive {

    @NotNull(message = "Vtuber ID should not be Empty")
    private UUID vtuberId;

    @NotNull(message = "Trigger label should not be Empty")
    @NotEmpty(message = "Trigger label should not be Empty")
    @NotBlank(message = "Trigger label should not be Empty")
    @Size(min = 3, max = 25, message = "Trigger label should be between 3 and 25 characters")
    @Pattern(regexp = "^[a-z0-9_-]{3,25}$", message = "Trigger label can have only English lower case letters, numbers, - and _ and be between 3 and 25 characters")
    private String triggerLabel;

    @NotNull(message = "Stat label should not be Empty")
    @NotEmpty(message = "Stat label should not be Empty")
    @NotBlank(message = "Stat label should not be Empty")
    @Size(min = 3, max = 25, message = "Stat label should be between 3 and 25 characters")
    @Pattern(regexp = "^[a-z0-9_-]{3,25}$", message = "Stat label can have only English lower case letters, numbers, - and _ and be between 3 and 25 characters")
    private String statLabel;

    private String value;

    @Pattern(regexp = "^[+*/=-]$")
    private String operator;

}
