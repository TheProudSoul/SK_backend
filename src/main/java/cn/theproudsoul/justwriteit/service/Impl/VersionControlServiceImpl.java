package cn.theproudsoul.justwriteit.service.Impl;

import cn.theproudsoul.justwriteit.constants.StorageProperties;
import cn.theproudsoul.justwriteit.web.exception.ExceedAccountRestrictionException;
import cn.theproudsoul.justwriteit.web.exception.StorageException;
import cn.theproudsoul.justwriteit.web.exception.StorageFileNotFoundException;
import cn.theproudsoul.justwriteit.persistence.model.VersionControlModel;
import cn.theproudsoul.justwriteit.persistence.repository.VersionControlRepository;
import cn.theproudsoul.justwriteit.service.VersionControlService;
import cn.theproudsoul.justwriteit.web.vo.VersionControlVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author TheProudSoul
 */
@Service
@Slf4j
public class VersionControlServiceImpl implements VersionControlService {
    private final Path rootLocation;
    private final Path fileRootLocation;

    private final VersionControlRepository versionControlRepository;

    public VersionControlServiceImpl(StorageProperties properties, VersionControlRepository versionControlRepository) {
        this.rootLocation = Paths.get(properties.getVersionControlLocation());
        this.fileRootLocation = Paths.get(properties.getFileLocation());
        log.info("version control location {}", rootLocation);
        this.versionControlRepository = versionControlRepository;
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }

    @Override
    public void store(long userId, String name) {
        if (versionControlRepository.getCountByUserId(userId) >= 15) {
            throw new ExceedAccountRestrictionException("UserId:" + userId + " try to store a new version ");
        }
        Path versionPath = this.rootLocation.resolve(String.valueOf(userId)).resolve(name);
        try (Stream<Path> stream = Files.walk(fileRootLocation.resolve(String.valueOf(userId)))) {
            Files.createDirectories(versionPath);
            stream.forEach(source -> copy(source, versionPath.resolve(fileRootLocation.resolve(String.valueOf(userId)).relativize(source))));
        } catch (IOException e) {
            throw new StorageException("Failed to version.", e);
        }
        VersionControlModel model = new VersionControlModel();
        model.setName(name);
        model.setUserId(userId);
        versionControlRepository.insert(model);
    }

    private void copy(Path source, Path dest) {
        try {
            Files.copy(source, dest, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public List<VersionControlVo> listAll(long userId) {
        return versionControlRepository.getUserVersionControlList(userId);
    }

    @Override
    public void loadAsResource(long userId, long id, OutputStream outputStream) throws IOException {

        try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
             ArchiveOutputStream out = new ZipArchiveOutputStream(bufferedOutputStream)
        ) {
            Path start = load(userId, id);
            Files.walkFileTree(start, new SimpleFileVisitor<>() {

                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    ArchiveEntry entry = new ZipArchiveEntry(dir.toFile(), start.relativize(dir).toString());
                    out.putArchiveEntry(entry);
                    out.closeArchiveEntry();
                    return super.preVisitDirectory(dir, attrs);
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    try (InputStream input = new FileInputStream(file.toFile())) {
                        ArchiveEntry entry = new ZipArchiveEntry(file.toFile(), start.relativize(file).toString());
                        out.putArchiveEntry(entry);
                        IOUtils.copy(input, out);
                        out.closeArchiveEntry();
                    }
                    return super.visitFile(file, attrs);
                }
            });
        }
    }

    @Override
    public boolean deleteVersion(long user, long id) {
        // 验证用户
        long userId = versionControlRepository.getUserIdById(id);
        if (userId == 0 || user != userId) {
            return false;
        }

        // 删除数据库数据
        versionControlRepository.delete(id);
        // 删除本地文件
        FileSystemUtils.deleteRecursively(load(user, id).toFile());
        return true;
    }

    private Path load(long userId, long id) {
        String name = versionControlRepository.getNameById(id);
        if (name == null) {
            throw new StorageFileNotFoundException("There is no such version for id " + id);
        }
        return rootLocation.resolve(String.valueOf(userId)).resolve(name);
    }
}
