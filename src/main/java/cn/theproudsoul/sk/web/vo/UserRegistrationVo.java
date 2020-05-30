package cn.theproudsoul.sk.web.vo;

import cn.theproudsoul.sk.web.validators.PasswordMatches;
import cn.theproudsoul.sk.web.validators.ValidEmail;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author TheProudSoul
 */
@PasswordMatches
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationVo {
    @NotNull
    @NotEmpty
    @ValidEmail
    private String email;

    @NotNull
    @NotEmpty
    private String username;

    @NotNull
    @Size(min = 8, max = 30)
    private String password;

    @NotNull
    @Size(min = 8, max = 30)
    private String confirmPassword;
}
