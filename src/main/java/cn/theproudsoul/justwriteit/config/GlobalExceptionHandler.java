package cn.theproudsoul.justwriteit.config;

import cn.theproudsoul.justwriteit.web.result.WebResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 * @author TheProudSoul
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public WebResult exceptionHandler (Exception ex) {
        return WebResult.error(ex.getMessage());
    }
}
