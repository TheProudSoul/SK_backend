package cn.theproudsoul.justwriteit.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author TheProudSoul
 */
@Getter
@Setter
public class UserModel {
    private long id;
    private String username;
    private String email;
    private String password;
    private boolean enabled;
    private boolean tokenExpired;
}
