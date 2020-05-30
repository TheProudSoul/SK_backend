package cn.theproudsoul.sk.web.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserVo {
    private long id;
    private String username;
    private String email;
}
