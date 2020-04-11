package cn.theproudsoul.justwriteit.service.Impl;

import cn.theproudsoul.justwriteit.constants.StorageProperties;
import cn.theproudsoul.justwriteit.exception.StorageException;
import cn.theproudsoul.justwriteit.exception.StorageFileNotFoundException;
import cn.theproudsoul.justwriteit.model.VersionControlModel;
import cn.theproudsoul.justwriteit.repository.VersionControlRepository;
import cn.theproudsoul.justwriteit.service.VersionControlService;
import cn.theproudsoul.justwriteit.web.vo.VersionControlVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.MalformedURLException;
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
//        log.info("version control location {}", fileRootLocation);
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
        try (Stream<Path> stream = Files.walk(fileRootLocation.resolve(String.valueOf(userId)))) {
            stream.forEach(source -> copy(source, this.rootLocation.resolve(String.valueOf(userId)).resolve(name).resolve(fileRootLocation.resolve(String.valueOf(userId)).relativize(source))));
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

        try (
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
                ArchiveOutputStream out = new ZipArchiveOutputStream(bufferedOutputStream);
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
                    try (
                            InputStream input = new FileInputStream(file.toFile())
                    ) {
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

    Path load(long userId, long id) {
        String name = versionControlRepository.getNameById(id);
        return rootLocation.resolve(String.valueOf(userId)).resolve(name);
    }
}
