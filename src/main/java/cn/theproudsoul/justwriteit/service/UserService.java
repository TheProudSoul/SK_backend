package cn.theproudsoul.justwriteit.service;

import cn.theproudsoul.justwriteit.web.exception.UserNotFoundException;
import cn.theproudsoul.justwriteit.persistence.model.UserModel;
import cn.theproudsoul.justwriteit.web.vo.UserLoginVo;
import cn.theproudsoul.justwriteit.web.vo.UserRegistrationVo;
import cn.theproudsoul.justwriteit.web.vo.UserVo;

/**
 * @author TheProudSoul
 */
public interface UserService {
    UserModel registerNewUserAccount(UserRegistrationVo accountVo);

    UserModel login(UserLoginVo user);

    UserVo loadUserByUserId(long userId) throws UserNotFoundException;
}
