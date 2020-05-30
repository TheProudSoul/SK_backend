package cn.theproudsoul.sk.service;

import cn.theproudsoul.sk.web.vo.VersionControlVo;



import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * @author TheProudSoul
 */
public interface VersionControlService {

    void init();

    void store(long userId, String name);

    List<VersionControlVo> listAll(long userId);

    void loadAsResource(long userId, long id, OutputStream outputStream) throws IOException;

    boolean deleteVersion(long user, long id);
}
