package cn.theproudsoul.justwriteit.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author TheProudSoul
 */
@Data
@EqualsAndHashCode
public class FileJournalModel {

    private long id;

    private long userId;

    private long journalId;

    private String path;

    private int eventType;
}
