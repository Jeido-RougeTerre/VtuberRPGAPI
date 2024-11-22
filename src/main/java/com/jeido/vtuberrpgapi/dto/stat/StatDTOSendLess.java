package com.jeido.vtuberrpgapi.dto.stat;

import com.jeido.vtuberrpgapi.dto.stat_influence.StatInfluenceDTOSend;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatDTOSendLess {
    private String label;
    private String value;
    private List<StatInfluenceDTOSend> influences;
}
