package cn.theproudsoul.justwriteit.web.controller;

import cn.theproudsoul.justwriteit.utils.JwtTokenUtil;
import cn.theproudsoul.justwriteit.constants.ControllerPath;
import cn.theproudsoul.justwriteit.persistence.model.UserModel;
import cn.theproudsoul.justwriteit.service.UserService;
import cn.theproudsoul.justwriteit.web.result.WebResult;
import cn.theproudsoul.justwriteit.web.vo.JwtTokenResponse;
import cn.theproudsoul.justwriteit.web.vo.UserLoginVo;
import cn.theproudsoul.justwriteit.web.vo.UserRegistrationVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author TheProudSoul
 */
@Slf4j
@RestController
@RequestMapping(ControllerPath.ACCOUNT)
public class AccountController {
    private final UserService userService;

    private final JwtTokenUtil jwtTokenUtil;

    public AccountController(UserService userService, JwtTokenUtil jwtTokenUtil) {
        this.userService = userService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    /**
     * 登录
     *
     * @param user email：邮箱 password：密码
     * @return 返回成功码
     */
    @PostMapping(path = "/login",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public JwtTokenResponse login(@Valid @RequestBody UserLoginVo user) {
        UserModel userModel = userService.login(user);
        final String token = jwtTokenUtil.generateToken(userModel);
        return new JwtTokenResponse(userModel.getId(), token);
    }

    /**
     * @param user email：邮箱 username:用户名 password：密码 confirmPassword：确认密码
     * @return 返回用户信息
     */
    @PostMapping(path = "/registration",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public WebResult registerUserAccount(@Valid @RequestBody UserRegistrationVo user) {
        log.debug("Registering user account with information: {}", user);
        return WebResult.success(userService.registerNewUserAccount(user));
    }

    @GetMapping("/registrationConfirm")
    public String confirmRegistration(@RequestParam("token") String token){
        // todo
        return null;
    }
}
