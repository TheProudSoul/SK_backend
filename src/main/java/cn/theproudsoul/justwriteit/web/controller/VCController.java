package cn.theproudsoul.justwriteit.web.controller;

import cn.theproudsoul.justwriteit.constants.ControllerPath;
import cn.theproudsoul.justwriteit.service.VersionControlService;
import cn.theproudsoul.justwriteit.utils.JwtTokenUtil;
import cn.theproudsoul.justwriteit.web.result.ERRORDetail;
import cn.theproudsoul.justwriteit.web.result.WebResult;
import cn.theproudsoul.justwriteit.web.vo.VersionControlVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author zhengyijing
 */
@RestController
@RequestMapping(ControllerPath.VC)
public class VCController {

    private final VersionControlService versionControlService;

    public VCController(VersionControlService versionControlService) {
        this.versionControlService = versionControlService;
    }

    /**
     * 获取用户现有版本列表
     *
     * @param user 用户 ID
     * @return 返回成功码
     */
    @GetMapping("/{user}")
    public WebResult getVersion(HttpServletRequest request, @PathVariable long user) {
        JwtTokenUtil.validateToken(request, user);
        return WebResult.success(versionControlService.listAll(user));
    }

    /**
     * 下载版本的 zip 包
     *
     * @param response 响应
     * @param user 用户 ID
     * @param id 版本 ID
     */
    @GetMapping("/{user}/{id}")
    public void getVersion(HttpServletRequest request, HttpServletResponse response, @PathVariable long user, @PathVariable long id) {
        JwtTokenUtil.validateToken(request, user);
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            versionControlService.loadAsResource(user, id, outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除一个版本
     *
     * @param user 用户 ID
     * @param id 版本 ID
     * @return 返回成功码
     */
    @DeleteMapping("/{user}/{id}")
    public WebResult deleteVersion(@PathVariable long user, @PathVariable long id) {
        // 检测用户权限
        if (versionControlService.deleteVersion(user, id)) {
            return WebResult.success();
        }
        return WebResult.error(ERRORDetail.RC_0303003);
    }

    /**
     * 储存一个版本
     *
     * @param user 用户 ID
     * @param vo name：设置版本名
     * @return 返回成功码
     */
    @PostMapping("/{user}")
    public WebResult handleVersionSave(@PathVariable long user, @RequestBody VersionControlVo vo) {
        if (vo.getName()==null) vo.setName(String.valueOf(System.currentTimeMillis()));
        versionControlService.store(user, vo.getName());
        return WebResult.success();
    }
}
