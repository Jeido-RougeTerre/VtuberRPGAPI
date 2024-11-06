package com.jeido.vtuberrpgapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTOReceive {
    @NotNull
    @Size(min = 3, max = 25, message = "Username should be between 3 and 25 characters")
    private String username;

    @NotNull
    @Size(min = 8, max = 25, message = "Password should be between 8 and 25 characters")
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$ %^&*-]).{8,}$", message = "Password should contains a minimum of eight characters, at least one upper case English letter, one lower case English letter, one number and one special character (#?!@$ %^&*-)")
    private String password;

    @NotNull
    @Email(message = "Invalid email")
    private String email;

}
