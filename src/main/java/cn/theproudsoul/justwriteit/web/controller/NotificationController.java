package cn.theproudsoul.justwriteit.web.controller;

import cn.theproudsoul.justwriteit.service.MetadataService;
import cn.theproudsoul.justwriteit.utils.JwtTokenUtil;
import cn.theproudsoul.justwriteit.web.result.WebResult;
import cn.theproudsoul.justwriteit.web.vo.FileJournalVo;
import cn.theproudsoul.justwriteit.web.vo.ListRequestVo;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import javax.servlet.http.HttpServletRequest;
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

    /**
     * 列出用户最新文件日志列表
     *
     * @param user 用户 ID
     * @param journalId 文件日志 ID
     * @return 后于参数文件日志 ID 的文件日志
     */
    @GetMapping("/{user}/list")
    public DeferredResult<List<FileJournalVo>> list(HttpServletRequest request, @PathVariable long user, @RequestParam long journalId) {
        JwtTokenUtil.validateToken(request, user);
        final DeferredResult<List<FileJournalVo>> result = new DeferredResult<>(null, Collections.emptyList());
        ListRequestVo listRequestVo = new ListRequestVo();
        listRequestVo.setJournalId(journalId);
        listRequestVo.setUserId(user);
        listRequestVo.setResult(result);
        listRequests.put(user, listRequestVo);

        result.onCompletion(() -> listRequests.remove(user, listRequestVo));

        List<FileJournalVo> list = metadataService.getLatestList(user, journalId);
        if (list != null && !list.isEmpty()) {
            result.setResult(list);
        }

        return result;
    }

//    /**
//     * @param vo 用户 ID, journalID, path, isDir
//     * @return
//     */
//    @PostMapping(value = "/commit", consumes = MediaType.APPLICATION_JSON_VALUE)
//    public WebResult commit(@RequestBody FileJournalVo vo) {
//        long journalId = metadataService.commit(vo.getUserId(), vo.getJournalId(), vo.getPath(), vo.isDir());
//        sendMessage(vo.getUserId());
//        return WebResult.success(journalId);
//    }

    /**
     * commit-delete
     *
     * @param vo userId, path, dir
     * @return 返回文件日志 ID
     */
    @PostMapping(value = "/commit-delete", consumes = MediaType.APPLICATION_JSON_VALUE)
    public WebResult commitDelete(HttpServletRequest request, @RequestBody FileJournalVo vo) {
        JwtTokenUtil.validateToken(request, vo.getUserId());
        long journalId = metadataService.commitDelete(vo.getUserId(), vo.getPath(), vo.isDir());
        sendMessage(vo.getUserId());
        return WebResult.success(journalId);
    }

    /**
     * commit-add
     *
     * @param vo userId, path, dir
     * @return 返回文件日志 ID
     */
    @PostMapping(value = "/commit-add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public WebResult commitAdd(HttpServletRequest request, @RequestBody FileJournalVo vo) {
        JwtTokenUtil.validateToken(request, vo.getUserId());
        long journalId = metadataService.commitAdd(vo.getUserId(), vo.getPath(), vo.isDir());
        sendMessage(vo.getUserId());
        return WebResult.success(journalId);
    }

    /**
     * commit-edit
     *
     * @param vo userId, path, data
     * @return 返回文件日志 ID
     */
    @PostMapping(value = "/commit-edit", consumes = MediaType.APPLICATION_JSON_VALUE)
    public WebResult commitEdit(HttpServletRequest request, @RequestBody FileJournalVo vo) {
        JwtTokenUtil.validateToken(request, vo.getUserId());
        long journalId = metadataService.commitEdit(vo.getUserId(), vo.getPath(), vo.getData());
        sendMessage(vo.getUserId());
        return WebResult.success(journalId);
    }

    /**
     * commit-edit
     *
     * @param vo userId, path, data
     * @return 返回文件日志 ID
     */
    @PostMapping(value = "/commit-move", consumes = MediaType.APPLICATION_JSON_VALUE)
    public WebResult commitMove(HttpServletRequest request, @RequestBody FileJournalVo vo) {
        JwtTokenUtil.validateToken(request, vo.getUserId());
        long journalId = metadataService.commitMove(vo.getUserId(), vo.getPath(), vo.getData());
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
