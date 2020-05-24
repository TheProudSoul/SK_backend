package cn.theproudsoul.justwriteit.service.Impl;

import cn.theproudsoul.justwriteit.web.exception.UserAlreadyExistException;
import cn.theproudsoul.justwriteit.web.exception.UserNotFoundException;
import cn.theproudsoul.justwriteit.persistence.model.UserModel;
import cn.theproudsoul.justwriteit.persistence.repository.UserRepository;
import cn.theproudsoul.justwriteit.service.UserService;
import cn.theproudsoul.justwriteit.web.vo.UserLoginVo;
import cn.theproudsoul.justwriteit.web.vo.UserRegistrationVo;
import cn.theproudsoul.justwriteit.web.vo.UserVo;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

/**
 * @author TheProudSoul
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserModel registerNewUserAccount(UserRegistrationVo accountVo) {
        if (emailExists(accountVo.getEmail())) {
            throw new UserAlreadyExistException("There is an account with that email address: " + accountVo.getEmail());
        }
        UserModel model = new UserModel();
        model.setUsername(accountVo.getUsername());
        model.setPassword(DigestUtils.md5DigestAsHex(accountVo.getPassword().getBytes()));
        model.setEmail(accountVo.getEmail());
        userRepository.save(model);
        return model;
    }

    @Override
    public UserModel login(UserLoginVo user) {
        UserModel userModel = userRepository.findByEmail(user.getEmail());
        if (userModel == null) return null;
        if (userModel.getPassword().equals(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()))) {
            return userModel;
        } else return null;
    }

    @Override
    public UserVo loadUserByUserId(long userId) throws UserNotFoundException {

        UserModel user = userRepository.findByUserId(userId);
        if (user == null) {
            throw new UserNotFoundException("User not found with userID: " + userId);
        }
        return UserVo.builder().id(userId).username(user.getUsername()).email(user.getEmail()).build();
    }

    private boolean emailExists(final String email) {
        return userRepository.findByEmail(email) != null;
    }
}
