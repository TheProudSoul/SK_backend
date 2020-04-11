package cn.theproudsoul.justwriteit.web.controller;

import cn.theproudsoul.justwriteit.constants.ControllerPath;
import cn.theproudsoul.justwriteit.model.UserModel;
import cn.theproudsoul.justwriteit.service.UserService;
import cn.theproudsoul.justwriteit.web.vo.UserLoginVo;
import cn.theproudsoul.justwriteit.web.vo.UserRegistrationVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author TheProudSoul
 */
@RestController
@RequestMapping(ControllerPath.ACCOUNT)
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping(path = "/login",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public long login(@Valid @RequestBody UserLoginVo user) {
        return userService.login(user);
    }

    @PostMapping(path = "/registration",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public UserModel registration(@Valid @RequestBody UserRegistrationVo user) {
        return userService.registerNewUserAccount(user);
    }
}
