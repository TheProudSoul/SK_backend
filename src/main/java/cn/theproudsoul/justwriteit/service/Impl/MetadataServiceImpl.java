package cn.theproudsoul.justwriteit.service.Impl;

import cn.theproudsoul.justwriteit.constants.EventType;
import cn.theproudsoul.justwriteit.web.vo.FileJournalVo;
import cn.theproudsoul.justwriteit.model.FileJournalModel;
import cn.theproudsoul.justwriteit.repository.FileJournalRepository;
import cn.theproudsoul.justwriteit.service.MetadataService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author TheProudSoul
 */
@Service
public class MetadataServiceImpl implements MetadataService {

    private final FileJournalRepository fileJournalRepository;

    public MetadataServiceImpl(FileJournalRepository fileJournalRepository) {
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

    private long getNextJournalId(long userId) {
        Long max = fileJournalRepository.getMaxJournalId(userId);
        if (max == null) {
            return 1;
        } else {
            return max + 1;
        }
    }
}
