package cn.theproudsoul.justwriteit.web.controller;

import cn.theproudsoul.justwriteit.constants.ControllerPath;
import cn.theproudsoul.justwriteit.model.UserModel;
import cn.theproudsoul.justwriteit.service.UserService;
import cn.theproudsoul.justwriteit.web.result.ERRORDetail;
import cn.theproudsoul.justwriteit.web.result.WebResult;
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

    /**
     * 登录
     *
     * @param user username：用户名 password：密码
     * @return 返回成功码
     */
    @PostMapping(path = "/login",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResult login(@Valid @RequestBody UserLoginVo user) {
        UserModel model = userService.login(user);
        if (model == null) return WebResult.error(ERRORDetail.RC_0303003);
        return WebResult.success(model);
    }

    /**
     * @param user email：邮箱 username:用户名 password：密码 confirmPassword：确认密码
     * @return 返回用户信息
     */
    @PostMapping(path = "/registration",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public WebResult registration(@Valid @RequestBody UserRegistrationVo user) {
        return WebResult.success(userService.registerNewUserAccount(user));
    }
}
