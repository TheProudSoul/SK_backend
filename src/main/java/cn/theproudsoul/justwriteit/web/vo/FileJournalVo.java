package cn.theproudsoul.justwriteit.web.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * @author TheProudSoul
 */
@Getter
@Setter
public class FileJournalVo {

    private long userId;

    private long journalId;

    private String path;

    private int eventType;
}
