package cn.theproudsoul.justwriteit.web.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author zhengyijing
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VersionControlVo {
    private long id;

    private String name;

    private String time;
}
