package cn.theproudsoul.justwriteit.web.controller;

import cn.theproudsoul.justwriteit.web.result.WebResult;
import cn.theproudsoul.justwriteit.web.vo.FileJournalVo;
import cn.theproudsoul.justwriteit.web.vo.ListRequestVo;
import cn.theproudsoul.justwriteit.service.MetadataService;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Collections;
import java.util.List;

/**
 * @author TheProudSoul
 * https://blog.csdn.net/zl1zl2zl3/article/details/83416067?depth_1-utm_source=distribute.pc_relevant.none-task&utm_source=distribute.pc_relevant.none-task
 */
@Slf4j
@RestController
public class NotificationController {
    private Multimap<Long, ListRequestVo> listRequests = Multimaps.synchronizedSetMultimap(HashMultimap.create());

    private final MetadataService metadataService;


    public NotificationController(MetadataService metadataService) {
        this.metadataService = metadataService;
    }

    @GetMapping("/list")
    public DeferredResult<List<FileJournalVo>> list(@RequestParam long userId, @RequestParam long journalId) {
        final DeferredResult<List<FileJournalVo>> result = new DeferredResult<>(null, Collections.emptyList());
        ListRequestVo listRequestVo = new ListRequestVo();
        listRequestVo.setJournalId(journalId);
        listRequestVo.setUserId(userId);
        listRequestVo.setResult(result);
        listRequests.put(userId, listRequestVo);

        result.onCompletion(() -> listRequests.remove(userId, listRequestVo));

        List<FileJournalVo> list = metadataService.getLatestList(userId, journalId);
        if (list != null && !list.isEmpty()) {
            result.setResult(list);
        }

        return result;
    }

    /**
     * @param vo 用户 ID, journalID, path
     * @return
     */
    @PostMapping(value = "/commit", consumes = MediaType.APPLICATION_JSON_VALUE)
    public WebResult commit(@RequestBody FileJournalVo vo) {
        long journalId = metadataService.commit(vo.getUserId(), vo.getJournalId(), vo.getPath());
        sendMessage(vo.getUserId());
        return WebResult.success(journalId);
    }

    /**
     * 为长查询返回数据
     *
     * @param user 用户 Id
     */
    private void sendMessage(long user) {
        if (listRequests.containsKey(user)) {
            listRequests.get(user).forEach(e -> e.getResult().setResult(metadataService.getLatestList(e.getUserId(), e.getJournalId())));
        }
        log.info("new list for: {}", user);
    }
}
