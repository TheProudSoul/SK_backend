package cn.theproudsoul.justwriteit.web.controller;

import cn.theproudsoul.justwriteit.constants.ControllerPath;
import cn.theproudsoul.justwriteit.persistence.model.UserModel;
import cn.theproudsoul.justwriteit.service.UserService;
import cn.theproudsoul.justwriteit.utils.JwtTokenUtil;
import cn.theproudsoul.justwriteit.web.result.WebResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author TheProudSoul
 */
@Slf4j
@RestController
@RequestMapping(ControllerPath.USER)
public class UserController {
    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{user}")
    public WebResult getUserInformation(HttpServletRequest request, @PathVariable long user){
        JwtTokenUtil.validateToken(request, user);
        return WebResult.success(userService.loadUserByUserId(user));
    }
}
