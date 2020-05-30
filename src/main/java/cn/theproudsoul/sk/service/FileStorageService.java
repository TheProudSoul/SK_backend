package cn.theproudsoul.sk.service;

import java.io.OutputStream;

/**
 * @author TheProudSoul
 */
public interface FileStorageService {
//    long MAX_FILE_UPLOAD_SIZE = 10485760;

    void init();

//    void store(MultipartFile file, long userId, String relativePath);

//    Stream<Path> loadAll(long userId);


    /**
     * @param user 用户 ID
     * @param filename 用户下载的文件路径
     * @return 返回用户请求的资源
     */
    String loadAsResource(long user, String filename, OutputStream outputStream);

//    void deleteAll();
}
