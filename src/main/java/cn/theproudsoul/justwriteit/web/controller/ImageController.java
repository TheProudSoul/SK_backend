package cn.theproudsoul.justwriteit.web.controller;

import cn.theproudsoul.justwriteit.constants.ControllerPath;
import cn.theproudsoul.justwriteit.web.result.ERRORDetail;
import cn.theproudsoul.justwriteit.web.result.Pagination;
import cn.theproudsoul.justwriteit.web.result.WebResult;
import cn.theproudsoul.justwriteit.service.ImageStorageService;
import cn.theproudsoul.justwriteit.web.vo.ImageHistoryVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
     * @param pagination 分页信息
     * @param user 用户 ID
     * @return 按分页返回用户上传图片
     */
    @GetMapping("/{user}")
    public List<ImageHistoryVo> listHistory(Pagination pagination, @PathVariable long user) {
        return imageStorageService.listAll(user);
    }

    /**
     * 获取用户上传图片总数
     * @param user 用户 ID
     * @return 按分页返回用户上传图片
     */
    @GetMapping("/{user}/total")
    public int getCount(@PathVariable long user) {
        return imageStorageService.getCount(user);
    }

    /**
     * 上传图片接口
     * @param file 图片文件
     * @param user 上传文件的用户 ID
     * @return 操作执行结果
     */
    @PostMapping("/{user}")
    public WebResult handleImageUpload(@RequestParam("file") MultipartFile file, @PathVariable long user) {
        // TODO 验证用户
        return WebResult.success(imageStorageService.store(file, user));
    }

    /**
     * 删除单张图片
     * @param userId 用户 ID
     * @param imageId 删除的图片 ID
     * @return 操作执行结果
     */
    @DeleteMapping("/{userId}/{imageId}")
    public WebResult handleImageDeleteOne(@PathVariable long userId, @PathVariable long imageId){
        // 检测用户权限
        if(imageStorageService.deleteOne(userId, imageId)){
            return WebResult.success();
        }
        return WebResult.error(ERRORDetail.RC_0303003);
    }

    /**
     * 删除所有图片
     * @param userId 用户 ID
     * @return 操作执行结果
     */
    @DeleteMapping("/{userId}")
    public WebResult handleImageDeleteAll(@PathVariable long userId){
        // 检测用户权限
        imageStorageService.deleteAll(userId);
        return WebResult.success();
    }
}
