package com.jeido.vtuberrpgapi.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatDTOReceive {
    @NotNull
    private UUID vtuberId;
    @NotEmpty
    @Size(min = 1, max = 100)
    private String label;
    private String value = "";
}
