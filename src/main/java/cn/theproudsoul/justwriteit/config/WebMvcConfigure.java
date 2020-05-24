package cn.theproudsoul.justwriteit.config;

import cn.theproudsoul.justwriteit.constants.StorageProperties;
import cn.theproudsoul.justwriteit.filter.ExceptionHandlerFilter;
import cn.theproudsoul.justwriteit.filter.JWTFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author TheProudSoul
 */
@Slf4j
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
        log.info("注册 JWT 过滤器");
        FilterRegistrationBean<JWTFilter> registration = new FilterRegistrationBean<>(jWTFilter);
        registration.setName("JWTFilter");
        registration.setOrder(Integer.MAX_VALUE);  // 这个order的默认值是Integer.MAX_VALUE 也就是int的最大值
        return registration;
    }

    @Bean
    public FilterRegistrationBean<ExceptionHandlerFilter> registration2() {
        log.info("注册 ExceptionHandler 过滤器");
        FilterRegistrationBean<ExceptionHandlerFilter> registration = new FilterRegistrationBean<>(exceptionHandlerFilter);
        registration.setName("ExceptionHandlerFilter");
        registration.setOrder(0);
        return registration;
    }


    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        log.info("注册 CORS 过滤器");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        // 是否允许证书 不再默认开启
//        config.setAllowCredentials(true);
        // 设置允许跨域请求的域名
        config.addAllowedOrigin("http://localhost:9080");
        config.addAllowedOrigin("http://localhost:8080");
        // 设置允许的方法
        config.addAllowedMethod("*");
        // 允许任何头
        config.addAllowedHeader("*");
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean<CorsFilter> registration = new FilterRegistrationBean<>(new CorsFilter(source));
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        registration.setName("CorsFilter");
        return registration;
    }
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins("http://localhost:9080", "http://localhost:8080")
//                .allowedMethods("*");
//    }

}
