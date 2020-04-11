package cn.theproudsoul.justwriteit.service;

import cn.theproudsoul.justwriteit.web.vo.FileSystemNode;

import java.util.List;

public interface ApiService {
    List<FileSystemNode> listFileSystem(long user);

    String readFile(long user, String pathName);
}
