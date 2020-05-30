package cn.theproudsoul.sk.web.vo;

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
public class FileSystemNode implements Comparable<FileSystemNode>  {
    String title;
    String pathName;
    String dirPath= "";
    boolean isLeaf;
    List<FileSystemNode> children;

    @Override
    public int compareTo(FileSystemNode o) {
        return this.title.compareTo(o.title);
    }
}
