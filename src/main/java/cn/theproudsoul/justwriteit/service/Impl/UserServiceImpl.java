package cn.theproudsoul.justwriteit.service.Impl;

import cn.theproudsoul.justwriteit.exception.UserAlreadyExistException;
import cn.theproudsoul.justwriteit.model.UserModel;
import cn.theproudsoul.justwriteit.repository.UserRepository;
import cn.theproudsoul.justwriteit.service.UserService;
import cn.theproudsoul.justwriteit.web.vo.UserLoginVo;
import cn.theproudsoul.justwriteit.web.vo.UserRegistrationVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        model.setPassword(accountVo.getPassword());
        model.setEmail(accountVo.getEmail());
        userRepository.save(model);
        return model;
    }

    @Override
    public UserModel login(UserLoginVo user) {
        UserModel userModel = userRepository.findByUsername(user.getUsername());
        if (userModel == null) return null;
        if (userModel.getPassword().equals(user.getPassword())) {
            return userModel;
        } else return null;
    }

    private boolean emailExists(final String email) {
        return userRepository.findByEmail(email) != null;
    }
}
