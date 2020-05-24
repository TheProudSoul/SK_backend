package cn.theproudsoul.justwriteit.config;

import cn.theproudsoul.justwriteit.constants.StorageProperties;
import cn.theproudsoul.justwriteit.filter.ExceptionHandlerFilter;
import cn.theproudsoul.justwriteit.filter.JWTFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author TheProudSoul
 */
@Configuration
public class WebMvcConfigure implements WebMvcConfigurer {

    private final StorageProperties storageProperties;
    private final JWTFilter jWTFilter;
    private final ExceptionHandlerFilter exceptionHandlerFilter;

    public WebMvcConfigure(StorageProperties storageProperties, JWTFilter jWTFilter, ExceptionHandlerFilter exceptionHandlerFilter) {
        this.storageProperties = storageProperties;
        this.jWTFilter = jWTFilter;
        this.exceptionHandlerFilter = exceptionHandlerFilter;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:" + storageProperties.getImagePathMapping());
    }

    @Bean
    public FilterRegistrationBean<JWTFilter> registration() {
        FilterRegistrationBean<JWTFilter> registration = new FilterRegistrationBean<>(jWTFilter);
        registration.setName("JWTFilter");
        registration.setOrder(Integer.MAX_VALUE);  // 这个order的默认值是Integer.MAX_VALUE 也就是int的最大值
        return registration;
    }

    @Bean
    public FilterRegistrationBean<ExceptionHandlerFilter> registration2() {
        FilterRegistrationBean<ExceptionHandlerFilter> registration = new FilterRegistrationBean<>(exceptionHandlerFilter);
        registration.setName("ExceptionHandlerFilter");
        registration.setOrder(Integer.MAX_VALUE - 1);
        return registration;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("http://localhost:9080", "http://localhost:8080")
                .allowedMethods( "*");
    }

}
