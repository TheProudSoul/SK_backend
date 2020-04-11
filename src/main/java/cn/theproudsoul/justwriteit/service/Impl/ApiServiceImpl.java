package cn.theproudsoul.justwriteit.service.Impl;

import cn.theproudsoul.justwriteit.constants.StorageProperties;
import cn.theproudsoul.justwriteit.exception.StorageFileNotFoundException;
import cn.theproudsoul.justwriteit.service.ApiService;
import cn.theproudsoul.justwriteit.web.vo.FileSystemNode;
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
        rootLocation = Paths.get(properties.getFileLocation());;
    }

    @Override
    public List<FileSystemNode> listFileSystem(long user) {
        Path path = rootLocation.resolve(String.valueOf(user));
        List<FileSystemNode> result = new ArrayList<>();
        FileSystemNode fileSystemNode = new FileSystemNode();
        File[] files = path.toFile().listFiles();
        for (File file : files) {
            FileSystemNode node = new FileSystemNode();
            node.setName(file.getName());
            node.setPathName(file.getPath().substring(path.toString().length()));
            if (file.isFile()){
                node.setLeaf(true);
            } else {
                node.setChildren(listFileSystemHelper(file, path));
            }
            System.out.println(file.getName());
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

    List<FileSystemNode> listFileSystemHelper(File dir, Path path) {
        List<FileSystemNode> result = new ArrayList<>();
        File[] files = dir.listFiles();
        for (File file : files) {
            FileSystemNode node = new FileSystemNode();
            node.setName(file.getName());
            node.setPathName(file.getPath().substring(path.toString().length()));
            if (file.isFile()){
                node.setLeaf(true);
            } else {
                node.setChildren(listFileSystemHelper(file, path));
            }
            System.out.println(file.getName());
            result.add(node);
        }
        return result;
    }
}
