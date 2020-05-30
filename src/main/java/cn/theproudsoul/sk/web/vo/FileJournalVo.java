package cn.theproudsoul.sk.web.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author TheProudSoul
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FileJournalVo {

    private long userId;

    private long journalId;

    private String path;

    private boolean dir;

    private int eventType;

    private String data;

    private String description;
}
