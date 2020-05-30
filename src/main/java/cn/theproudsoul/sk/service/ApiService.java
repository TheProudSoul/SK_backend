package cn.theproudsoul.sk.service;

import cn.theproudsoul.sk.web.vo.FileSystemNode;

import java.util.List;

public interface ApiService {
    List<FileSystemNode> listFileSystem(long user);

    String readFile(long user, String pathName);
}
