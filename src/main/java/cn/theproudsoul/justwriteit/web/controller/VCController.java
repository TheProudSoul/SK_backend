package cn.theproudsoul.justwriteit.web.controller;

import cn.theproudsoul.justwriteit.constants.ControllerPath;
import cn.theproudsoul.justwriteit.service.VersionControlService;
import cn.theproudsoul.justwriteit.web.result.WebResult;
import cn.theproudsoul.justwriteit.web.vo.VersionControlVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import org.springframework.core.io.Resource;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @author zhengyijing
 */
@RestController
@RequestMapping(ControllerPath.VC)
public class VCController {

    @Autowired
    private VersionControlService versionControlService;

    @GetMapping("/{user}")
    public WebResult getVersion(@PathVariable long user) {
        return WebResult.success(versionControlService.listAll(user));
    }

    @GetMapping("/{user}/{id}")
    public void getVersion(HttpServletResponse response, @PathVariable long user, @PathVariable long id) {
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            versionControlService.loadAsResource(user, id, outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 储存一个版本
     */
    @PostMapping("/{user}")
    public WebResult handleVersionSave(@PathVariable long user, @RequestBody VersionControlVo vo) {
        if (vo.getName()==null) vo.setName(String.valueOf(System.currentTimeMillis()));
        versionControlService.store(user, vo.getName());
        return WebResult.success();
    }
}
