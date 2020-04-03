package cn.theproudsoul.justwriteit.web.vo;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author TheProudSoul
 */
@Getter
@Setter
public class UserLoginVo {

    @NotNull
    @NotEmpty
    private String username;

    @NotNull
    @Size(min = 8, max = 30)
    private String password;
}
