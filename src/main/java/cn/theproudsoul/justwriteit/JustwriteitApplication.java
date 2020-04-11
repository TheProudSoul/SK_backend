package cn.theproudsoul.justwriteit;

import cn.theproudsoul.justwriteit.constants.StorageProperties;
import cn.theproudsoul.justwriteit.service.FileStorageService;
import cn.theproudsoul.justwriteit.service.ImageStorageService;
import cn.theproudsoul.justwriteit.service.VersionControlService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class JustwriteitApplication {

    public static void main(String[] args) {
        SpringApplication.run(JustwriteitApplication.class, args);
    }

    @Bean
    CommandLineRunner init(ImageStorageService imageStorageService, VersionControlService versionControlService, FileStorageService fileStorageService) {
        return (args) -> {
//            storageService.deleteAll();
            imageStorageService.init();
            versionControlService.init();
            fileStorageService.init();
        };
    }
}
