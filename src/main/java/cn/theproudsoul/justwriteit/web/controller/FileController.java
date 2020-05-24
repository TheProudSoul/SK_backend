package cn.theproudsoul.justwriteit.web.controller;

import cn.theproudsoul.justwriteit.constants.ControllerPath;
import cn.theproudsoul.justwriteit.service.FileStorageService;
import cn.theproudsoul.justwriteit.utils.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author TheProudSoul
 */
@Slf4j
@Controller
@RequestMapping(ControllerPath.FILE)
public class FileController {

    private final FileStorageService storageService;

    public FileController(FileStorageService storageService) {
        this.storageService = storageService;
    }
//
//    @PostMapping("/store")
//    @ResponseBody
//    public WebResult store(@RequestParam("file") MultipartFile file, @RequestBody StoreFileVo vo) {
//        storageService.store(file, vo.getUserId(), vo.getPathName());
//        return WebResult.success();
//    }

    /**
     * 下载文件
     *
     * @param path 文件路径
     * @param user 用户 ID
     */
    @GetMapping("/{user}/retrieve")
    public void retrieve(HttpServletRequest request, HttpServletResponse response, @RequestParam String path, @PathVariable long user) {
        JwtTokenUtil.validateToken(request, user);
        try {
            ServletOutputStream outputStream = response.getOutputStream();
//            outputStream.write(data.getBytes(StandardCharsets.UTF_8));
//            outputStream.close();
            String fileName = storageService.loadAsResource(user, path, outputStream);
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + fileName + "\"");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
