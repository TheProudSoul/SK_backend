package cn.theproudsoul.sk.web.result;

import lombok.Getter;
import lombok.Setter;

/**
 * @author TheProudSoul
 */
@Getter
@Setter
public class Pagination {
    // https://www.baeldung.com/rest-api-pagination-in-spring
    private int pageNum = 1;
    private int pageSize = 10;
}
