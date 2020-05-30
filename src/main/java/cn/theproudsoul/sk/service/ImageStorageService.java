package cn.theproudsoul.sk.service;

import cn.theproudsoul.sk.web.vo.ImageHistoryVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author TheProudSoul
 */
public interface ImageStorageService {
    long MAX_FILE_UPLOAD_SIZE = 10485760;

    void init();

    String store(MultipartFile file, long userId);

    List<ImageHistoryVo> listAll(long userId, int offset, int limit);

//    Path load(String filename);

//    Path loadAsResource(long user, String filename);

    boolean deleteAll(long userId);

    boolean deleteOne(long userId, long imageId);

    int getCount(long user);
}
