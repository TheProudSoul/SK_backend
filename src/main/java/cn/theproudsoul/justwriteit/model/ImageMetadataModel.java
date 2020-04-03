package cn.theproudsoul.justwriteit.model;

import lombok.Data;

/**
 * @author TheProudSoul
 */
@Data
public class ImageMetadataModel {

    private long id;

    private long userId;

    private String imageUrl;

    private String originName;
}
