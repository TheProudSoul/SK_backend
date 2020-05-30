package cn.theproudsoul.sk.service.Impl;

import cn.theproudsoul.sk.constants.EventType;
import cn.theproudsoul.sk.constants.StorageProperties;
import cn.theproudsoul.sk.web.exception.StorageException;
import cn.theproudsoul.sk.web.exception.StorageFileNotFoundException;
import cn.theproudsoul.sk.persistence.model.FileJournalModel;
import cn.theproudsoul.sk.persistence.repository.FileJournalRepository;
import cn.theproudsoul.sk.service.MetadataService;
import cn.theproudsoul.sk.web.vo.FileJournalVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.*;
import java.util.List;

/**
 * @author TheProudSoul
 */
@Service
@Slf4j
public class MetadataServiceImpl implements MetadataService {

    private final FileJournalRepository fileJournalRepository;
    private final Path fileRootLocation;

    public MetadataServiceImpl(StorageProperties properties, FileJournalRepository fileJournalRepository) {
        this.fileRootLocation = Paths.get(properties.getFileLocation());
        this.fileJournalRepository = fileJournalRepository;
    }

    @Override
    public List<FileJournalVo> getLatestList(long userId, long journalId) {
        return fileJournalRepository.getLatestList(userId, journalId);
    }

//    @Override
//    public long commit(long userId, long journalId, String path, boolean isDir) {
//        FileJournalModel model = new FileJournalModel();
//        if (isDir) {
//            model.setEventType(EventType.ADD_DIR.ordinal());
//        } else {
//            model.setEventType(EventType.ADD_FILE.ordinal());
//        }
//        model.setUserId(userId);
//        model.setPath(path);
//        journalId = getNextJournalId(userId);
//        model.setJournalId(journalId);
//        fileJournalRepository.insert(model);
//        return journalId;
//    }

    @Override
    public long commitDelete(long userId, String path, boolean isDir) {
        long journalId;
        try {
            if (isDir) {
                journalId = insertFileJournal(userId, path, EventType.DELETE_DIR, "");
                FileSystemUtils.deleteRecursively(fileRootLocation.resolve(String.valueOf(userId)).resolve(path).toFile());
            } else {
                journalId = insertFileJournal(userId, path, EventType.DELETE_FILE, "");
                Files.delete(fileRootLocation.resolve(String.valueOf(userId)).resolve(path));
            }
        } catch (IOException e) {
            throw new StorageFileNotFoundException("cant find file for path: " + path);
        }
        return journalId;
    }

    @Override
    public long commitAdd(long userId, String path, boolean dir) {

        Path userDirector = this.fileRootLocation.resolve(String.valueOf(userId));
        Path filePath = userDirector.resolve(path);
        long journalId = 0;
        try {
            if (Files.notExists(userDirector)) {
                Files.createDirectories(userDirector);
            }
            log.info("user with ID {} try to create {}", userId, filePath.toString());
            if (dir) {
                Files.createDirectories(filePath);
                journalId = insertFileJournal(userId, path, EventType.ADD_DIR, "");
            } else {
                Files.createFile(filePath);
                journalId = insertFileJournal(userId, path, EventType.ADD_FILE, "");

            }
        } catch (IOException e) {
            throw new StorageException("Could not create this path:", e);
        }
        return journalId;
    }

    @Override
    public long commitEdit(long userId, String path, String data) {
        Path filePath = this.fileRootLocation.resolve(String.valueOf(userId)).resolve(path);
        try (PrintWriter p = new PrintWriter(Files.newOutputStream(filePath))) {
            p.write(data);
        } catch (IOException e) {
            throw new StorageException("Could not create this path: " + filePath.toString());
        }
        return insertFileJournal(userId, path, EventType.EDIT, "");
    }

    @Override
    public long commitMove(long userId, String path, String data) {
        Path oldPath = this.fileRootLocation.resolve(String.valueOf(userId)).resolve(data);
        Path newPath = this.fileRootLocation.resolve(String.valueOf(userId)).resolve(path);
        try {
            Files.move(oldPath, newPath,
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            log.error("Could not move from: " + oldPath.toString()+" to " + newPath.toString(), e);
            throw new StorageException("Could not move from: " + path +" to "+ data);
        }
        return insertFileJournal(userId, data, EventType.MOVE, path);
    }

    private long insertFileJournal(long userId, String path, EventType eventType, String description) {
        FileJournalModel model = new FileJournalModel();
        model.setEventType(eventType.ordinal());
        model.setUserId(userId);
        model.setPath(path);
        model.setDescription(description);
        long journalId = getNextJournalId(userId);
        model.setJournalId(journalId);
        fileJournalRepository.insert(model);
        return journalId;
    }

    private long getNextJournalId(long userId) {
        Long max = fileJournalRepository.getMaxJournalId(userId);
        if (max == null) {
            return 1;
        } else {
            return max + 1;
        }
    }
}
