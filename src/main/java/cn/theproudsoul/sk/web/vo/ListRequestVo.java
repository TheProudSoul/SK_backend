package cn.theproudsoul.sk.web.vo;

import lombok.Data;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;

/**
 * @author TheProudSoul
 */
@Data
public class ListRequestVo {
    long userId;
    long journalId;
    DeferredResult<List<FileJournalVo>> result;

}
