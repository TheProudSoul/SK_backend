package cn.theproudsoul.justwriteit.service.Impl;

import cn.theproudsoul.justwriteit.constants.StorageProperties;
import cn.theproudsoul.justwriteit.web.exception.StorageException;
import cn.theproudsoul.justwriteit.persistence.model.VersionControlModel;
import cn.theproudsoul.justwriteit.persistence.repository.VersionControlRepository;
import cn.theproudsoul.justwriteit.web.vo.VersionControlVo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class VersionControlServiceImplTest {
    private final long userId = 2;

    private VersionControlServiceImpl service;

    @Mock
    VersionControlRepository repository;

    private final StorageProperties properties = new StorageProperties();

    @BeforeEach
    void init() throws Exception {
        MockitoAnnotations.initMocks(this);
        long randomLong = Math.abs(new Random().nextLong());
        properties.setVersionControlLocation("target/VC/" + randomLong);
        properties.setFileLocation("target/files");
        Files.createDirectories(Path.of("target/files", "2", "testDir"));
        service = new VersionControlServiceImpl(properties, repository);
        service.init();
    }

    @Test
    void store() {
        String versionName = "Version One";
        VersionControlModel versionOne = new VersionControlModel();
        versionOne.setName(versionName);
        versionOne.setUserId(userId);
        service.store(userId, versionName);
        verify(repository, times(1)).insert(Mockito.eq(versionOne));
    }

    @Test
    void listAll() {
        List<VersionControlVo> list = new ArrayList<>();
        VersionControlVo versionOne = new VersionControlVo(1, "Version One", new Date().toString());
        VersionControlVo versionTwo = new VersionControlVo(2, "Version Two", new Date().toString());
        VersionControlVo versionThree = new VersionControlVo(3, "Version Three", new Date().toString());

        list.add(versionOne);
        list.add(versionTwo);
        list.add(versionThree);

        when(repository.getUserVersionControlList(userId)).thenReturn(list);

        //test
        List<VersionControlVo> versionList = service.listAll(userId);

        assertEquals(3, versionList.size());
        verify(repository, times(1)).getUserVersionControlList(userId);
    }

    @Test
    void deleteVersion() {
        when(repository.getNameById(3)).thenReturn("Version Three");
        when(repository.getUserIdById(3)).thenReturn(userId);
        service.deleteVersion(userId, 3);
        verify(repository, times(1)).delete(3);
    }

    @Test
    void loadAsResource() throws Exception {
        String versionName = "Version One";
        String fakeData = "fakeData";
        final String fakePath = "fakePathI\\";
        final String fakeFile = "fakeFile.md";
        Files.createDirectories(Path.of(properties.getVersionControlLocation(), "2", versionName, fakePath));
        try (PrintWriter p = new PrintWriter(Files.newOutputStream(Path.of(properties.getVersionControlLocation(), "2",
                versionName, fakePath, fakeFile), StandardOpenOption.CREATE))) {
            p.write(fakeData);
        } catch (IOException e) {
            throw new StorageException("Could not create this file: " + fakeFile);
        }
        when(repository.getNameById(1)).thenReturn(versionName);

        // test file not exists
        ByteArrayOutputStream finalOutputStream = new ByteArrayOutputStream();
//        Exception exception = assertThrows(StorageFileNotFoundException.class, () -> {
//            service.loadAsResource(userId, "notExists", finalOutputStream);
//        });
//        assertEquals("Could not read file: notExists", exception.getMessage());

        // test directory
        service.loadAsResource(userId, 1, finalOutputStream);
        assertTrue(finalOutputStream.toString().contains(fakeFile));
    }
}