package cn.theproudsoul.justwriteit.web.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author zhengyijing
 */
@Getter
@Setter
public class VersionControlVo {
    private String id;
    private String name;

    private Date time;
}
