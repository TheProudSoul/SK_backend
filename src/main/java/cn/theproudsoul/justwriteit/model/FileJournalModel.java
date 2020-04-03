package cn.theproudsoul.justwriteit.model;

import lombok.Data;

/**
 * @author TheProudSoul
 */
@Data
public class FileJournalModel {

    private long id;

    private long userId;

    private long journalId;

    private String path;

    private int eventType;
}
