package cn.theproudsoul.justwriteit.web.controller;

import cn.theproudsoul.justwriteit.constants.ControllerPath;
import cn.theproudsoul.justwriteit.service.ApiService;
import cn.theproudsoul.justwriteit.web.result.WebResult;
import cn.theproudsoul.justwriteit.web.vo.FileSystemNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author zhengyijing
 */
@RestController
@RequestMapping(ControllerPath.API)
public class ApiController {
    @Autowired
    private ApiService apiService;

    @GetMapping("/file-system/{user}")
    public WebResult listFileSystem(@PathVariable long user){
        List<FileSystemNode> nodes = apiService.listFileSystem(user);
        return WebResult.success(nodes);
    }

    @GetMapping("/file-system/{user}/file")
    public WebResult readFile(@PathVariable long user, @RequestParam String path){
        String nodes = apiService.readFile(user, path);
        return WebResult.success(nodes);
    }

    @PostMapping("/file-system/{user}/file")
    public WebResult writeFile(@PathVariable long user, @RequestBody String path){
        String nodes = apiService.readFile(user, path);
        return WebResult.success(nodes);
    }
}
