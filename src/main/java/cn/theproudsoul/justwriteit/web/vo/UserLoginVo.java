package cn.theproudsoul.justwriteit.web.vo;

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
    private String username;

    @NotNull
    @Size(min = 8, max = 30)
    private String password;
}
