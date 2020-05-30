package cn.theproudsoul.sk.service.Impl;

import cn.theproudsoul.sk.constants.StorageProperties;
import cn.theproudsoul.sk.web.exception.StorageException;
import cn.theproudsoul.sk.web.exception.StorageFileNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class FileSystemStorageServiceImplTest {

    private final long userId = 2;

    private FileSystemStorageServiceImpl service;

    private final StorageProperties properties = new StorageProperties();

    @BeforeEach
    void init() throws Exception {
        MockitoAnnotations.initMocks(this);
        long randomLong = Math.abs(new Random().nextLong());
        properties.setFileLocation("target/files/" + randomLong);
        Files.createDirectories(Path.of("target/files/" + randomLong, "2"));
        service = new FileSystemStorageServiceImpl(properties);
        service.init();
    }

    @Test
    void loadAsResource() throws Exception {
        String fakeData = "fakeData";
        final String fakePath = "fakePathI\\";
        final String fakeFile = "fakeFile.md";
        Files.createDirectories(Path.of(properties.getFileLocation(), "2", fakePath));
        try (PrintWriter p = new PrintWriter(Files.newOutputStream(Path.of(properties.getFileLocation(), "2",
                fakePath, fakeFile), StandardOpenOption.CREATE))) {
            p.write(fakeData);
        } catch (IOException e) {
            throw new StorageException("Could not create this file: " + fakeFile);
        }

        // test file
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        service.loadAsResource(userId, fakePath + fakeFile, outputStream);
        assertEquals(fakeData, outputStream.toString());

        // test file not exists
        ByteArrayOutputStream finalOutputStream = new ByteArrayOutputStream();
        Exception exception = assertThrows(StorageFileNotFoundException.class, () -> service.loadAsResource(userId, "notExists", finalOutputStream));
        assertEquals("There is no such file: notExists", exception.getMessage());

        // test directory
        service.loadAsResource(userId, fakePath, outputStream);
        assertTrue(outputStream.toString().contains(fakeData));
    }
}