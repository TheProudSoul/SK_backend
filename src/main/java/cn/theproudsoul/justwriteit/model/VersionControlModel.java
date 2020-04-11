package cn.theproudsoul.justwriteit.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author TheProudSoul
 */
@Getter
@Setter
public class VersionControlModel {
    private long id;
    private long userId;
    private String name;
    private Date updateTime;
}
