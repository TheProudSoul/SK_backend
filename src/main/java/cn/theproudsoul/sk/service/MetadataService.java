package cn.theproudsoul.sk.service;

import cn.theproudsoul.sk.web.vo.FileJournalVo;

import java.util.List;

/**
 * @author TheProudSoul
 */
public interface MetadataService {
    List<FileJournalVo> getLatestList(long userId, long journalId);

//    long commit(long userId, long journalId, String path, boolean isDir);

    long commitDelete(long userId, String path, boolean isDir);

    long commitAdd(long userId, String path, boolean dir);

    long commitEdit(long userId, String path, String data);

    long commitMove(long userId, String path, String data);
}
