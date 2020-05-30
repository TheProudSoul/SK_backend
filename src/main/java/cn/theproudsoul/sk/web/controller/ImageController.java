package cn.theproudsoul.sk.web.controller;

import cn.theproudsoul.sk.constants.ControllerPath;
import cn.theproudsoul.sk.service.ImageStorageService;
import cn.theproudsoul.sk.utils.JwtTokenUtil;
import cn.theproudsoul.sk.web.result.ERRORDetail;
import cn.theproudsoul.sk.web.result.Pagination;
import cn.theproudsoul.sk.web.result.WebResult;
import cn.theproudsoul.sk.web.vo.ImageHistoryVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author TheProudSoul
 */
@Slf4j
@RestController
@RequestMapping(ControllerPath.IMAGE)
public class ImageController {

    private final ImageStorageService imageStorageService;

    public ImageController(ImageStorageService imageStorageService) {
        this.imageStorageService = imageStorageService;
    }

    /**
     * 用户历史上传图片
     *
     * @param pagination 分页信息
     * @param user       用户 ID
     * @return 按分页返回用户上传图片
     */
    @GetMapping(path = "/{user}")
    public WebResult listHistory(HttpServletRequest request, Pagination pagination, @PathVariable long user) {
        JwtTokenUtil.validateToken(request, user);
        return WebResult.success(imageStorageService.listAll(user, (pagination.getPageNum() - 1) * pagination.getPageSize(), pagination.getPageSize()));
    }

    /**
     * 获取用户上传图片总数
     *
     * @param user 用户 ID
     * @return 按分页返回用户上传图片
     */
    @GetMapping("/{user}/total")
    public int getCount(HttpServletRequest request, @PathVariable long user) {
        JwtTokenUtil.validateToken(request, user);
        return imageStorageService.getCount(user);
    }

    /**
     * 上传图片接口
     *
     * @param file 图片文件
     * @param user 上传文件的用户 ID
     * @return 操作执行结果
     */
    @PostMapping("/{user}")
    public WebResult handleImageUpload(HttpServletRequest request, @RequestParam("file") MultipartFile file, @PathVariable long user) {
        JwtTokenUtil.validateToken(request, user);
        return WebResult.success(imageStorageService.store(file, user));
    }

    /**
     * 删除单张图片
     *
     * @param user  用户 ID
     * @param imageId 删除的图片 ID
     * @return 操作执行结果
     */
    @DeleteMapping("/{user}/{imageId}")
    public WebResult handleImageDeleteOne(HttpServletRequest request, @PathVariable long user, @PathVariable long imageId) {
        JwtTokenUtil.validateToken(request, user);
        if (imageStorageService.deleteOne(user, imageId)) {
            return WebResult.success();
        }
        return WebResult.error(ERRORDetail.RC_0303003);
    }

    /**
     * 删除所有图片
     *
     * @param user 用户 ID
     * @return 操作执行结果
     */
    @DeleteMapping("/{user}")
    public WebResult handleImageDeleteAll(HttpServletRequest request, @PathVariable long user) {
        JwtTokenUtil.validateToken(request, user);
        if (imageStorageService.deleteAll(user)) {
            return WebResult.success();
        }
        return WebResult.error("failed");
    }
}
