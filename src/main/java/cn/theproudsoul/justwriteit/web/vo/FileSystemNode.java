package cn.theproudsoul.justwriteit.web.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author zhengyijing
 */
@Getter
@Setter
public class FileSystemNode {
    String name;
    String pathName;
    boolean isLeaf;
    List<FileSystemNode> children;
}
