package cn.theproudsoul.sk;

import cn.theproudsoul.sk.constants.StorageProperties;
import cn.theproudsoul.sk.service.FileStorageService;
import cn.theproudsoul.sk.service.ImageStorageService;
import cn.theproudsoul.sk.service.VersionControlService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class SuperKnowledgeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SuperKnowledgeApplication.class, args);
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
