package com.jeido.vtuberrpgapi.dto.user;

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
public class UserDTOSend {
    private UUID userId;

    private String username;

    private String email;

    private boolean isAdmin;

    private List<UUID> vtuberIds;
}
