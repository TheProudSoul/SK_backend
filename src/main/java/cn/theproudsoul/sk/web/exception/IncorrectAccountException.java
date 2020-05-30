package cn.theproudsoul.sk.web.exception;

public class IncorrectAccountException extends RuntimeException {

    public IncorrectAccountException(String message) {
        super(message);
    }

    public IncorrectAccountException(String message, Throwable cause) {
        super(message, cause);
    }
}