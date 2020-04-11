package cn.theproudsoul.justwriteit.service.Impl;

import cn.theproudsoul.justwriteit.constants.EventType;
import cn.theproudsoul.justwriteit.constants.StorageProperties;
import cn.theproudsoul.justwriteit.exception.StorageFileNotFoundException;
import cn.theproudsoul.justwriteit.web.vo.FileJournalVo;
import cn.theproudsoul.justwriteit.model.FileJournalModel;
import cn.theproudsoul.justwriteit.repository.FileJournalRepository;
import cn.theproudsoul.justwriteit.service.MetadataService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author TheProudSoul
 */
@Service
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

    @Override
    public long commit(long userId, long journalId, String path) {
        FileJournalModel model = new FileJournalModel();
        model.setEventType(EventType.ADD.ordinal());
        model.setUserId(userId);
        model.setPath(path);
        journalId = getNextJournalId(userId);
        model.setJournalId(journalId);
        fileJournalRepository.insert(model);
        return journalId;
    }

    @Override
    public long commitDelete(long userId, long journalId, String path) {
        FileJournalModel model = new FileJournalModel();
        model.setEventType(EventType.DELETE.ordinal());
        model.setUserId(userId);
        model.setPath(path);
        journalId = getNextJournalId(userId);
        model.setJournalId(journalId);
        try {
            Files.delete(fileRootLocation.resolve(String.valueOf(userId)).resolve(path));
        } catch (IOException e) {
            throw new StorageFileNotFoundException("cant find file for path: " + path);
        }
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
