package com.jeido.vtuberrpgapi.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTOLogin {
    @NotNull(message = "Username or E-mail should not be null")
    @NotBlank(message = "Username or E-mail should not be empty")
    private String usernameOrEmail;

    @NotNull(message = "Password should not be null")
    @NotBlank(message = "Password should not be empty")
    private String password;
}
