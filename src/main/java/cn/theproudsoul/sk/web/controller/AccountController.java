package cn.theproudsoul.sk.web.controller;

import cn.theproudsoul.sk.utils.JwtTokenUtil;
import cn.theproudsoul.sk.constants.ControllerPath;
import cn.theproudsoul.sk.persistence.model.UserModel;
import cn.theproudsoul.sk.service.UserService;
import cn.theproudsoul.sk.web.result.WebResult;
import cn.theproudsoul.sk.web.vo.JwtTokenResponse;
import cn.theproudsoul.sk.web.vo.UserLoginVo;
import cn.theproudsoul.sk.web.vo.UserRegistrationVo;
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
    public WebResult login(@Valid @RequestBody UserLoginVo user) {
        UserModel userModel = userService.login(user);
        final String token = jwtTokenUtil.generateToken(userModel);
        return WebResult.success(new JwtTokenResponse(userModel.getId(), token));
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
