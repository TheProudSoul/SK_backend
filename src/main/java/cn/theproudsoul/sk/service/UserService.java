package cn.theproudsoul.sk.service;

import cn.theproudsoul.sk.web.exception.UserNotFoundException;
import cn.theproudsoul.sk.persistence.model.UserModel;
import cn.theproudsoul.sk.web.vo.UserLoginVo;
import cn.theproudsoul.sk.web.vo.UserRegistrationVo;
import cn.theproudsoul.sk.web.vo.UserVo;

/**
 * @author TheProudSoul
 */
public interface UserService {
    UserModel registerNewUserAccount(UserRegistrationVo accountVo);

    UserModel login(UserLoginVo user);

    UserVo loadUserByUserId(long userId) throws UserNotFoundException;
}
