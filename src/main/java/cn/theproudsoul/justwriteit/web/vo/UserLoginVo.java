package cn.theproudsoul.justwriteit.web.vo;

import cn.theproudsoul.justwriteit.web.validators.ValidEmail;
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
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginVo {

    @NotNull
    @NotEmpty
    @ValidEmail
    private String email;

    @NotNull
    @Size(min = 8, max = 30)
    private String password;
}
