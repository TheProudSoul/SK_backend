package cn.theproudsoul.sk.service.Impl;

import cn.theproudsoul.sk.constants.StorageProperties;
import cn.theproudsoul.sk.web.exception.StorageFileNotFoundException;
import cn.theproudsoul.sk.service.ApiService;
import cn.theproudsoul.sk.web.vo.FileSystemNode;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhengyijing
 */
@Service
public class ApiServiceImpl implements ApiService {
    private final Path rootLocation;

    public ApiServiceImpl(StorageProperties properties) {
        rootLocation = Paths.get(properties.getFileLocation());
    }

    @Override
    public List<FileSystemNode> listFileSystem(long user) {
        Path path = rootLocation.resolve(String.valueOf(user));
        List<FileSystemNode> result = new ArrayList<>();
        File[] files = path.toFile().listFiles();
        if (files==null) return result;
        for (File file : files) {
            FileSystemNode node = new FileSystemNode();
            node.setTitle(file.getName());
            node.setPathName(file.getPath().substring(path.toString().length() + 1));
            node.setDirPath(node.getPathName().substring(0, node.getPathName().length()-node.getTitle().length()));

            if (file.isFile()) {
                node.setLeaf(true);
            } else {
                node.setChildren(listFileSystemHelper(file, path));
            }
            result.add(node);
        }
        return result;
    }

    @Override
    public String readFile(long user, String pathName) {
        if (pathName.startsWith("/")) pathName = pathName.substring(1);
        Path path = rootLocation.resolve(String.valueOf(user)).resolve(pathName);
        try {
            return Files.readString(path);
        } catch (IOException e) {
            throw new StorageFileNotFoundException("there is no such file.");
        }
    }

    private List<FileSystemNode> listFileSystemHelper(File dir, Path path) {
        List<FileSystemNode> result = new ArrayList<>();
        File[] files = dir.listFiles();
        if (files==null) return result;
        for (File file : files) {
            FileSystemNode node = new FileSystemNode();
            node.setTitle(file.getName());
            node.setPathName(file.getPath().substring(path.toString().length() + 1));
            node.setDirPath(node.getPathName().substring(0, node.getPathName().length()-node.getTitle().length()));
            if (file.isFile()) {
                node.setLeaf(true);
            } else {
                node.setChildren(listFileSystemHelper(file, path));
            }
            result.add(node);
        }
        return result;
    }
}
