package com.jeido.vtuberrpgapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatDTOSend {
    @JsonIgnore
    private UUID vtuberId;
    private String label;
    private String value;
}
