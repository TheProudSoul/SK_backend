package cn.theproudsoul.sk.web.vo;

import lombok.*;

/**
 * @author TheProudSoul
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ImageHistoryVo {

    private long id;

    private String imageUrl;
    private String scaleUrl;
}
