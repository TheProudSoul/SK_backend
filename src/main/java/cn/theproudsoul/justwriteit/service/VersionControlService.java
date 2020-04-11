package cn.theproudsoul.justwriteit.service;

import cn.theproudsoul.justwriteit.web.vo.VersionControlVo;



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
}
