package cn.theproudsoul.justwriteit.constants;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zhengyijing
 */
@Getter
@Setter
@ConfigurationProperties("storage")
public class StorageProperties {
    /**
     * Folder location for storing files
     */
    private String fileLocation = "upload-dir";
    private String imageLocation = "image-upload-dir";
    private String versionControlLocation = "version-control-dir";
    private String imagePathMapping = "/appdata/justwriteit/images/";
}
