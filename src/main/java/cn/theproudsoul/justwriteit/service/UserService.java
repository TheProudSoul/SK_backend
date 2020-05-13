package cn.theproudsoul.justwriteit.service;

import cn.theproudsoul.justwriteit.model.UserModel;
import cn.theproudsoul.justwriteit.web.vo.UserLoginVo;
import cn.theproudsoul.justwriteit.web.vo.UserRegistrationVo;

/**
 * @author TheProudSoul
 */
public interface UserService {
    UserModel registerNewUserAccount(UserRegistrationVo accountVo);

    UserModel login(UserLoginVo user);
}
