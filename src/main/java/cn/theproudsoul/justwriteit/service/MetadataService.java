package cn.theproudsoul.justwriteit.service;

import cn.theproudsoul.justwriteit.web.vo.FileJournalVo;

import java.util.List;

/**
 * @author TheProudSoul
 */
public interface MetadataService {
    List<FileJournalVo> getLatestList(long userId, long journalId);

    long commit(long userId, long journalId, String path);
    long commitDelete(long userId, long journalId, String path);
}
