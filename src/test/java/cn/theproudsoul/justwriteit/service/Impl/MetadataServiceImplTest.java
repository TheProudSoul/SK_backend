package cn.theproudsoul.justwriteit.service.Impl;

import cn.theproudsoul.justwriteit.constants.StorageProperties;
import cn.theproudsoul.justwriteit.web.exception.StorageException;
import cn.theproudsoul.justwriteit.persistence.model.FileJournalModel;
import cn.theproudsoul.justwriteit.persistence.repository.FileJournalRepository;
import cn.theproudsoul.justwriteit.web.vo.FileJournalVo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
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
import static org.mockito.Mockito.*;

class MetadataServiceImplTest {
    private final long userId = 2;
    private final String fakePath = "fakePath";
    private final String fakeFile = "fakeFile.md";

    private MetadataServiceImpl service;

    @Mock
    FileJournalRepository repository;

    private final StorageProperties properties = new StorageProperties();

    @BeforeEach
    void init() throws Exception {
        MockitoAnnotations.initMocks(this);
        long randomLong = Math.abs(new Random().nextLong());

        properties.setFileLocation("target/files/" + randomLong+"/");
        Files.createDirectories(Path.of(properties.getFileLocation(), "2"));

        service = new MetadataServiceImpl(properties, repository);
    }

    @Test
    void getLatestList() {
        List<FileJournalVo> list = new ArrayList<>();
        FileJournalVo journalVoI = new FileJournalVo(0, 6, fakePath, false, 0, null);
        FileJournalVo journalVoII = new FileJournalVo(0, 7, fakePath, false, 0, null);
        FileJournalVo journalVoIII = new FileJournalVo(0, 8, fakePath, false, 0, null);
        list.add(journalVoI);
        list.add(journalVoII);
        list.add(journalVoIII);
        when(repository.getLatestList(userId, 5)).thenReturn(list);

        //test
        List<FileJournalVo> versionList = service.getLatestList(userId, 5);

        assertEquals(3, versionList.size());
        verify(repository, times(1)).getLatestList(userId, 5);
    }

    @Test
    void commitDelete() throws Exception {

        try (PrintWriter p = new PrintWriter(Files.newOutputStream(Path.of(properties.getFileLocation(), "2", fakeFile), StandardOpenOption.CREATE))) {
            String fakeData = "fakeData";
            p.write(fakeData);
        } catch (IOException e) {
            throw new StorageException("Could not create this file: " + fakeFile);
        }

        long expectedResult = 5;
        FileJournalModel model = new FileJournalModel();
        model.setEventType(1);
        model.setUserId(userId);
        model.setPath(fakeFile);
        when(repository.insert(Mockito.eq(model))).thenReturn(1);
        when(repository.getMaxJournalId(userId)).thenReturn(expectedResult - 1);
        assertEquals(service.commitDelete(userId, fakeFile, false), expectedResult);

        Files.createDirectories(Path.of(properties.getFileLocation(), "2", fakePath));
        model.setEventType(3);
        model.setPath(fakePath);
        when(repository.insert(Mockito.eq(model))).thenReturn(1);
        when(repository.getMaxJournalId(userId)).thenReturn(expectedResult - 1);
        //test
        assertEquals(service.commitDelete(userId, fakePath, true), expectedResult);
    }

    @Test
    void commitAdd() {
        long expectedResult = 5;
        FileJournalModel model = new FileJournalModel();
        model.setEventType(2);
        model.setUserId(userId);
        model.setPath(fakePath);
        when(repository.insert(Mockito.eq(model))).thenReturn(1);
        when(repository.getMaxJournalId(userId)).thenReturn(expectedResult - 1);
        // test add directory
        assertEquals(service.commitAdd(userId, fakePath, true), expectedResult);
        assertTrue(Files.exists(Path.of(properties.getFileLocation()+ userId,fakePath)));


        model.setEventType(0);
        model.setUserId(userId);
        model.setPath(fakeFile);
        when(repository.insert(Mockito.eq(model))).thenReturn(1);
        when(repository.getMaxJournalId(userId)).thenReturn(expectedResult - 1);
        // test add file
        assertEquals(service.commitAdd(userId, fakeFile, false), expectedResult);
        assertTrue(Path.of(properties.getFileLocation()+ userId,fakeFile).toFile().isFile());
    }

    @Test
    void commitEdit() {
        long expectedResult = 1;
        FileJournalModel model = new FileJournalModel();
        model.setEventType(0);
        model.setUserId(userId);
        model.setPath(fakeFile);
        when(repository.insert(Mockito.eq(model))).thenReturn(1);
        when(repository.getMaxJournalId(userId)).thenReturn(null);
        //test
        assertEquals(service.commitEdit(userId, fakeFile, "data"), expectedResult);
    }
}