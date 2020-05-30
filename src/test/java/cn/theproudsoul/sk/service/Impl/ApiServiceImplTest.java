package cn.theproudsoul.sk.service.Impl;

import cn.theproudsoul.sk.constants.StorageProperties;
import cn.theproudsoul.sk.web.exception.StorageException;
import cn.theproudsoul.sk.web.vo.FileSystemNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class ApiServiceImplTest {
    private final long userId = 2;
    private final String fakePath = "fakePathI\\fakePathII\\";
    private final String fakeFile = "fakeFile.md";

    private ApiServiceImpl service;

    private final StorageProperties properties = new StorageProperties();

    @BeforeEach
    void init() throws Exception {
        MockitoAnnotations.initMocks(this);
        long randomLong = Math.abs(new Random().nextLong());

        properties.setFileLocation("target/files/" + randomLong);
        Files.createDirectories(Path.of(properties.getFileLocation(), "2"));

        service = new ApiServiceImpl(properties);
    }

    @Test
    void listFileSystem() throws Exception {
        Files.createDirectories(Path.of(properties.getFileLocation(), "2", fakePath));
        try (PrintWriter p = new PrintWriter(Files.newOutputStream(Path.of(properties.getFileLocation(), "2", fakePath, fakeFile), StandardOpenOption.CREATE))) {
            String fakeData = "fakeData";
            p.write(fakeData);
        } catch (IOException e) {
            throw new StorageException("Could not create this file: " + fakeFile);
        }

        List<FileSystemNode> expectedResult = new ArrayList<>();
        FileSystemNode node = new FileSystemNode();
        FileSystemNode nodeChild = new FileSystemNode();
        node.setTitle("fakePathI");
        node.setPathName("fakePathI");
        node.setDirPath("");
        nodeChild.setTitle("fakePathII");
        nodeChild.setPathName("fakePathI\\fakePathII");
        nodeChild.setDirPath("fakePathI\\");
        node.setChildren(List.of(nodeChild));
        FileSystemNode nodeFile = new FileSystemNode();
        nodeFile.setTitle(fakeFile);
        nodeFile.setPathName(fakePath + fakeFile);
        nodeFile.setDirPath(fakePath);
        nodeFile.setLeaf(true);
        nodeChild.setChildren(List.of(nodeFile));
        expectedResult.add(node);

        List<FileSystemNode> result = service.listFileSystem(userId);
        assertEquals(result, expectedResult);
    }

    @Test
    void readFile() throws Exception {
        String fakeData = "fakeData";
        Files.createDirectories(Path.of(properties.getFileLocation(), "2", fakePath));
        try (PrintWriter p = new PrintWriter(Files.newOutputStream(Path.of(properties.getFileLocation(), "2",
                fakePath, fakeFile), StandardOpenOption.CREATE))) {
            p.write(fakeData);
        } catch (IOException e) {
            throw new StorageException("Could not create this file: " + fakeFile);
        }
        assertEquals(fakeData, service.readFile(userId, fakePath+fakeFile));
    }
}