package com.jeido.vtuberrpgapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTOSend {
    private UUID userId;

    private String username;

    private String email;

    private boolean isAdmin;
}
