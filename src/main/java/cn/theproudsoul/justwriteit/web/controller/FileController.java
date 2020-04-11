package cn.theproudsoul.justwriteit.web.controller;

import cn.theproudsoul.justwriteit.constants.ControllerPath;
import cn.theproudsoul.justwriteit.web.result.WebResult;
import cn.theproudsoul.justwriteit.exception.StorageFileNotFoundException;
import cn.theproudsoul.justwriteit.service.FileStorageService;
import cn.theproudsoul.justwriteit.web.vo.StoreFileVo;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author TheProudSoul
 */
@RestController
@RequestMapping(ControllerPath.FILE)
public class FileController {

    private final FileStorageService storageService;

    public FileController(FileStorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping("/store")
    public WebResult store(@RequestParam("file") MultipartFile file, @RequestBody StoreFileVo vo) {
        storageService.store(file, vo.getUserId(), vo.getPathName());
        return WebResult.success();
    }

    @GetMapping("/{user}/retrieve/{filename}")
    public ResponseEntity<Resource> retrieve(@PathVariable String filename, @PathVariable long user) {
        Resource file = storageService.loadAsResource(user, filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
}
