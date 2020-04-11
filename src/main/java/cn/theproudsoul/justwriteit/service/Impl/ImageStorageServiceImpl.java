package cn.theproudsoul.justwriteit.service.Impl;

import cn.theproudsoul.justwriteit.constants.StorageProperties;
import cn.theproudsoul.justwriteit.exception.StorageException;
import cn.theproudsoul.justwriteit.model.ImageMetadataModel;
import cn.theproudsoul.justwriteit.repository.ImageMetadataRepository;
import cn.theproudsoul.justwriteit.service.ImageStorageService;
import cn.theproudsoul.justwriteit.web.vo.ImageHistoryVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

/**
 * @author TheProudSoul
 */
@Slf4j
@Service
public class ImageStorageServiceImpl implements ImageStorageService {
    private final Path rootLocation;

    private final ImageMetadataRepository imageMetadataRepository;

    @Autowired
    public ImageStorageServiceImpl(StorageProperties properties, ImageMetadataRepository imageMetadataRepository) {
        this.rootLocation = Paths.get(properties.getImageLocation());
        log.info("image location {}", rootLocation);
        this.imageMetadataRepository = imageMetadataRepository;
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
    public String store(MultipartFile file, long userId) {
        String originFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String extension = StringUtils.getFilenameExtension(originFilename);
        String newName = UUID.randomUUID().toString();
        // TODO enum 类型
        if (!checkPicturesExtension(extension)) {
            throw new StorageException("图片类型不支持！");
        }
        long size = file.getSize();
        if (size == 0 || size > MAX_FILE_UPLOAD_SIZE) {
            throw new IllegalArgumentException("文件必须在0-10M之间");
        }
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + originFilename);
            }
            if (originFilename.contains("..")) {
                // This is a security check
                throw new StorageException("Cannot store file with relative path outside current directory " + originFilename);
            }
            Path userDirector = this.rootLocation.resolve(String.valueOf(userId));
            if (Files.notExists(userDirector)) {
                Files.createDirectories(userDirector);
            }
            ImageMetadataModel model = new ImageMetadataModel();
            model.setOriginName(originFilename);
            model.setUserId(userId);
            model.setImageUrl("http://justwriteit.theproudsoul.cn/images/" + userId + "/" + newName + '.' + extension);
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, userDirector.resolve(newName + '.' + extension),
                        StandardCopyOption.REPLACE_EXISTING);
                imageMetadataRepository.insert(model);
                return imageMetadataRepository.getById(model.getId()).getImageUrl();
            }
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + originFilename, e);
        }
    }

    @Override
    public List<ImageHistoryVo> listAll(long userId) {
        return imageMetadataRepository.getUserImageList(userId);
    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public Path loadAsResource(long user, String filename) {
        Path file = load(user + File.separator + filename);
        return file;
//        try {
//            Resource resource = new UrlResource(file.toUri());
//            if (resource.exists() || resource.isReadable()) {
//                return resource;
//            } else {
//                throw new StorageFileNotFoundException(
//                        "Could not read file: " + filename);
//
//            }
//        } catch (MalformedURLException e) {
//            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
//        }
    }

    @Override
    public boolean deleteAll(long userId) {
        imageMetadataRepository.deleteAll(userId);
        FileSystemUtils.deleteRecursively(rootLocation.resolve(String.valueOf(userId)).toFile());
        return true;
    }

    @Override
    public boolean deleteOne(long userId, long imageId) {
        ImageMetadataModel model = imageMetadataRepository.getById(imageId);
        if (model == null || model.getUserId() != userId) {
            return false;
        }
        Path filePath = rootLocation.resolve(String.valueOf(userId)).resolve(StringUtils.getFilename(model.getImageUrl()));
        try {
            if (Files.exists(filePath)) {
                // https://blog.csdn.net/LuoZheng4698729/article/details/51697648
                Files.delete(filePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        imageMetadataRepository.deleteOne(imageId);
        return true;
    }

    @Override
    public int getCount(long user) {
        return imageMetadataRepository.count(user);
    }

    /**
     * 检测图片后缀
     *
     * @param extension
     * @return
     */
    private boolean checkPicturesExtension(String extension) {
        if (null == extension) {
            return false;
        }
        return true;
//        return PropType.DRAW_ALLOW_UPLOAD_FILE_EXT.contains(extension);
    }
}
