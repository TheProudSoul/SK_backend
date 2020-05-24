package cn.theproudsoul.justwriteit.config;

import cn.theproudsoul.justwriteit.web.exception.ExceedAccountRestrictionException;
import cn.theproudsoul.justwriteit.web.exception.StorageFileNotFoundException;
import cn.theproudsoul.justwriteit.web.exception.UnauthorizedException;
import cn.theproudsoul.justwriteit.web.result.ERRORDetail;
import cn.theproudsoul.justwriteit.web.result.ExceptionResponse;
import cn.theproudsoul.justwriteit.web.result.WebResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * 全局异常处理
 * @author TheProudSoul
 */
@RestControllerAdvice
@Slf4j
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    // 400
    @Override
    protected ResponseEntity<Object> handleBindException(final BindException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        logger.error("400 Status Code", ex);
        final BindingResult result = ex.getBindingResult();
        final ExceptionResponse bodyOfResponse = new ExceptionResponse(result.getAllErrors(), "Invalid" + result.getObjectName());
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        logger.error("400 Status Code", ex);
        final BindingResult result = ex.getBindingResult();
        final ExceptionResponse bodyOfResponse = new ExceptionResponse(result.getAllErrors(), "Invalid " + result.getObjectName());
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({ Exception.class })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleInternal(final RuntimeException ex, final WebRequest request) {
        logger.error("500 Status Code", ex);
        return new ResponseEntity<>(WebResult.error(ex.getMessage()), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Object> handleUnauthorizedException(UnauthorizedException ex, final HttpStatus status, final WebRequest request) {
        final ExceptionResponse bodyOfResponse = new ExceptionResponse(ex.getMessage(), ex.getMessage());
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(StorageFileNotFoundException.class)
    public WebResult handleStorageFileNotFound(StorageFileNotFoundException ex) {
        return WebResult.error(ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ExceedAccountRestrictionException.class)
    public WebResult handleExceedAccountRestrictionExceptions(ExceedAccountRestrictionException ex) {
        return WebResult.error(ERRORDetail.RC_0303004);
    }
}
