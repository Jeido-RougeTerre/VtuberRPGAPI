package com.jeido.vtuberrpgapi.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatDTOReceiveLess {

    @Size(min = 3, max = 25, message = "Label should be between 3 and 25 characters")
    private String label;
    private String value;
}
