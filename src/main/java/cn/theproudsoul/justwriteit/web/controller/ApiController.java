package cn.theproudsoul.justwriteit.web.controller;

import cn.theproudsoul.justwriteit.constants.ControllerPath;
import cn.theproudsoul.justwriteit.service.ApiService;
import cn.theproudsoul.justwriteit.utils.JwtTokenUtil;
import cn.theproudsoul.justwriteit.web.result.WebResult;
import cn.theproudsoul.justwriteit.web.vo.FileSystemNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 *  为网页和手机端提供接口
 *
 * @author zhengyijing
 */
@RestController
@RequestMapping(ControllerPath.API)
public class ApiController {

    private final ApiService apiService;

    public ApiController(ApiService apiService) {
        this.apiService = apiService;
    }

    /**
     * 将用户文件结构整理为 List
     *
     * @param user 用户 ID
     * @return 返回用户文件系统结构
     */
    @GetMapping("/file-system/{user}")
    public WebResult listFileSystem(HttpServletRequest request, @PathVariable long user){
        JwtTokenUtil.validateToken(request, user);
        List<FileSystemNode> nodes = apiService.listFileSystem(user);
        return WebResult.success(nodes);
    }

    /**
     * 读取文件内容
     *
     * @param user 用户 ID
     * @param path 文件路径
     * @return 返回文件内容
     */
    @GetMapping("/file-system/{user}/file")
    public WebResult readFile(HttpServletRequest request, @PathVariable long user, @RequestParam String path){
        JwtTokenUtil.validateToken(request, user);
        String nodes = apiService.readFile(user, path);
        return WebResult.success(nodes);
    }
}
