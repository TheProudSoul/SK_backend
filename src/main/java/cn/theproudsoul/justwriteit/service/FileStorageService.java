package cn.theproudsoul.justwriteit.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * @author TheProudSoul
 */
public interface FileStorageService {
    long MAX_FILE_UPLOAD_SIZE = 10485760;

    void init();

    void store(MultipartFile file, long userId, String relativePath);

    Stream<Path> loadAll(long userId);

    Path load(String filename);

    Resource loadAsResource(long user, String filename);

    void deleteAll();
}
