package cn.theproudsoul.justwriteit.web.vo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author zhengyijing
 */
@Getter
@Setter
@EqualsAndHashCode
public class FileSystemNode {
    String title;
    String pathName;
    String dirPath= "";
    boolean isLeaf;
    List<FileSystemNode> children;
}
