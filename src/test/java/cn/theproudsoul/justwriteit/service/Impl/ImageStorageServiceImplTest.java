package cn.theproudsoul.justwriteit.service.Impl;

import cn.theproudsoul.justwriteit.constants.StorageProperties;
import cn.theproudsoul.justwriteit.web.exception.StorageException;
import cn.theproudsoul.justwriteit.persistence.model.ImageMetadataModel;
import cn.theproudsoul.justwriteit.persistence.repository.ImageMetadataRepository;
import cn.theproudsoul.justwriteit.web.vo.ImageHistoryVo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ImageStorageServiceImplTest {
    private final long userId = 2;

    private ImageStorageServiceImpl service;

    @Mock
    ImageMetadataRepository repository;

    private final StorageProperties properties = new StorageProperties();

    private String uploadImageName = "Ziggy Stardust.jpg";

    @BeforeEach
    void init() {
        MockitoAnnotations.initMocks(this);
        long randomLong = Math.abs(new Random().nextLong());
        properties.setImageLocation("target/images/" + randomLong+"/");
//        Files.createDirectories(Path.of("target/images/" + randomLong));
        service = new ImageStorageServiceImpl(properties, repository);
        service.init();
    }

    @Test
    void store() throws Exception {
        String expectedResult = uploadImageName;
        MockMultipartFile image = new MockMultipartFile("file", expectedResult, "image/jpg",
                this.getClass().getClassLoader().getResourceAsStream(uploadImageName));
        when(repository.insert(any(ImageMetadataModel.class))).thenReturn(1);
        ImageMetadataModel model = new ImageMetadataModel();
        model.setId(1);
        model.setUserId(userId);
        model.setImageUrl(expectedResult);
        when(repository.getById(Mockito.anyLong())).thenReturn(model);

        String result = service.store(image, userId);
        assertEquals(expectedResult, result);
        assertTrue(Objects.requireNonNull(Path.of(properties.getImageLocation(),
                String.valueOf(userId)).toFile().listFiles()).length > 0);
        verify(repository, times(1)).insert(any(ImageMetadataModel.class));

        MockMultipartFile multipartFile = new MockMultipartFile("file", "test",
                "text/plain", "Spring Framework".getBytes());
        Exception exception = assertThrows(StorageException.class, () -> service.store(multipartFile, userId));
        assertEquals("图片类型不支持！", exception.getMessage());

        String relativePath = "../" + expectedResult;
        MockMultipartFile imageWithRelativePath = new MockMultipartFile("file", relativePath, "image/jpg",
                this.getClass().getClassLoader().getResourceAsStream(uploadImageName));
        exception = assertThrows(StorageException.class, () -> service.store(imageWithRelativePath, userId));
        assertEquals("Cannot store file with relative path outside current directory " + relativePath, exception.getMessage());

        MockMultipartFile imageEmpty = new MockMultipartFile("file", expectedResult, "image/jpg", new byte[0]);
        exception = assertThrows(IllegalArgumentException.class, () -> service.store(imageEmpty, userId));
        assertEquals("文件必须在0-10M之间", exception.getMessage());
    }

    @Test
    void listAll() {
        String fakeUrl = "fakeUrl";
        ImageHistoryVo historyVoI = new ImageHistoryVo(1, fakeUrl);
        ImageHistoryVo historyVoII = new ImageHistoryVo(2, fakeUrl);
        ImageHistoryVo historyVoIII = new ImageHistoryVo(3, fakeUrl);
        ImageHistoryVo historyVoIIII = new ImageHistoryVo(4, fakeUrl);
        List<ImageHistoryVo> list = new ArrayList<>();
        list.add(historyVoI);
        list.add(historyVoII);
        list.add(historyVoIII);
        list.add(historyVoIIII);
        when(repository.getUserImageList(userId, 0, 15)).thenReturn(list);

        assertEquals(list, service.listAll(userId, 0, 15));
    }

    @Test
    void deleteAll() throws Exception {
        when(repository.deleteAll(userId)).thenReturn(1);
        Files.copy(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream(uploadImageName)), Path.of(properties.getImageLocation() + userId));
        service.deleteAll(userId);
        assertNull(Path.of(properties.getImageLocation(), String.valueOf(userId)).toFile().listFiles());
        verify(repository, times(1)).deleteAll(userId);
    }

    @Test
    void deleteOne() throws Exception {
        Path userPath = Files.createDirectory(Path.of(properties.getImageLocation(), "2"));
        ImageMetadataModel model = new ImageMetadataModel();
        model.setId(1);
        model.setUserId(userId);
        model.setImageUrl(uploadImageName);
        when(repository.getById(Mockito.anyLong())).thenReturn(model);
        when(repository.deleteOne(1)).thenReturn(1);

        // test success
        Files.copy(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream(uploadImageName)),
                userPath.resolve(uploadImageName), StandardCopyOption.REPLACE_EXISTING);
        assertTrue(service.deleteOne(userId, 1));

        verify(repository, times(1)).deleteOne(1);
        assertEquals(0, Objects.requireNonNull(Path.of(properties.getImageLocation(),
                String.valueOf(userId)).toFile().listFiles()).length);

        // test fail
        when(repository.getById(5)).thenReturn(null);
        assertFalse(service.deleteOne(userId, 5));
    }

    @Test
    void getCount() {
        when(repository.count(userId)).thenReturn(1);
        assertEquals(1, service.getCount(userId));
    }
}