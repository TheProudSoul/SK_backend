package cn.theproudsoul.justwriteit.service.Impl;

import cn.theproudsoul.justwriteit.exception.UserAlreadyExistException;
import cn.theproudsoul.justwriteit.repository.UserRepository;
import cn.theproudsoul.justwriteit.web.vo.UserLoginVo;
import cn.theproudsoul.justwriteit.web.vo.UserRegistrationVo;
import cn.theproudsoul.justwriteit.model.UserModel;
import cn.theproudsoul.justwriteit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author TheProudSoul
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

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
    public long login(UserLoginVo user) {
        UserModel userModel = userRepository.findByUsername(user.getUsername());
        if (userModel.getPassword().equals(user.getPassword())){
            return userModel.getId();
        } else return -1;
    }

    private boolean emailExists(final String email) {
        return userRepository.findByEmail(email) != null;
    }
}
