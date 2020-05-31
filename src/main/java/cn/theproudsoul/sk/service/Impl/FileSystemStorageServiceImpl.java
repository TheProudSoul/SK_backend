package cn.theproudsoul.sk.service.Impl;

import cn.theproudsoul.sk.constants.StorageProperties;
import cn.theproudsoul.sk.web.exception.StorageException;
import cn.theproudsoul.sk.web.exception.StorageFileNotFoundException;
import cn.theproudsoul.sk.service.FileStorageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * @author TheProudSoul
 */
@Service
@Slf4j
public class FileSystemStorageServiceImpl implements FileStorageService {
    private final Path rootLocation;

    public FileSystemStorageServiceImpl(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getFileLocation());
        log.info("file store location {}", rootLocation);
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }

//    @Override
//    public void store(MultipartFile file, long userId, String relativePath) {
//        String filename = StringUtils.cleanPath(file.getOriginalFilename());
//
//        try {
//            if (file.isEmpty()) {
//                throw new StorageException("Failed to store empty file " + filename);
//            }
//            if (filename.contains("..")) {
//                // This is a security check
//                throw new StorageException("Cannot store file with relative path outside current directory " + filename);
//            }
//            try (InputStream inputStream = file.getInputStream()) {
//                Files.copy(inputStream, this.rootLocation.resolve(String.valueOf(userId)).resolve(relativePath).resolve(filename),
//                        StandardCopyOption.REPLACE_EXISTING);
//            }
//        } catch (IOException e) {
//            throw new StorageException("Failed to store file " + filename, e);
//        }
//    }
//
//    @Override
//    public Stream<Path> loadAll(long userId) {
//        Path location = this.rootLocation.resolve(String.valueOf(userId));
//        try {
//            return Files.walk(location, 1)
//                    .filter(path -> !path.equals(location))
//                    .map(location::relativize);
//        } catch (IOException e) {
//            throw new StorageException("Failed to read stored files", e);
//        }
//    }

    private Path load(long user, String filename) {
        return rootLocation.resolve(String.valueOf(user)).resolve(filename);
    }

    @Override
    public String loadAsResource(long user, String filename, OutputStream outputStream) {
        Path file = load(user, filename);
        if (!Files.exists(file)){
            throw new StorageFileNotFoundException("There is no such file: "+filename);
        }
        try {
            if (file.toFile().isFile()) {
                Files.copy(file, outputStream);
            } else {
                try (
                        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
                        ArchiveOutputStream out = new ZipArchiveOutputStream(bufferedOutputStream)
                ) {
                    Path start = file;
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
                return file.toFile().getName();
        } catch (IOException e) {
            throw new StorageException("Could not read file: " + filename, e);
        }
    }
//
//    @Override
//    public void deleteAll() {
//        FileSystemUtils.deleteRecursively(rootLocation.toFile());
//    }
}
