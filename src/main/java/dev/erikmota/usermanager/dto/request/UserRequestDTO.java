package dev.erikmota.usermanager.dto.request;

import dev.erikmota.usermanager.annotations.EmailValidate;
import dev.erikmota.usermanager.annotations.MandatoryField;
import dev.erikmota.usermanager.annotations.PasswordValidate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {

    @MandatoryField(name = "Nome")
    private String name;

    @MandatoryField(name = "Login")
    private String login;

    @PasswordValidate(name = "Senha")
    private String password;

    private String confirmPassword;

    @MandatoryField(name = "Email")
    @EmailValidate(name = "Email")
    private String email;

    @MandatoryField(name = "Status")
    private Boolean enabled;
}
