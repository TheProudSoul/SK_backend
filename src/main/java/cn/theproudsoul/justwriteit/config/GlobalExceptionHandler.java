package cn.theproudsoul.justwriteit.config;

import cn.theproudsoul.justwriteit.exception.ExceedAccountRestrictionException;
import cn.theproudsoul.justwriteit.exception.StorageFileNotFoundException;
import cn.theproudsoul.justwriteit.web.result.ERRORDetail;
import cn.theproudsoul.justwriteit.web.result.WebResult;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常处理
 * @author TheProudSoul
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(StorageFileNotFoundException.class)
    public WebResult handleStorageFileNotFound(StorageFileNotFoundException ex) {
        return WebResult.error(ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public WebResult handleValidationExceptions(MethodArgumentNotValidException ex) {
        JSONObject jsonObject = new JSONObject();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            jsonObject.put(fieldName, errorMessage);
        });
        return WebResult.error(jsonObject.toJSONString());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ExceedAccountRestrictionException.class)
    public WebResult handleExceedAccountRestrictionExceptions(ExceedAccountRestrictionException ex) {
        return WebResult.error(ERRORDetail.RC_0303004);
    }

    @ExceptionHandler(Exception.class)
    public WebResult exceptionHandler (Exception ex) {
        return WebResult.error(ex.getMessage());
    }

}
