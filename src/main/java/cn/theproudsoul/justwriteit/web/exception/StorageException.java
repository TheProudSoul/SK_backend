package cn.theproudsoul.justwriteit.web.exception;

/**
 * @author TheProudSoul
 */
public class StorageException extends RuntimeException {

    public StorageException(String message) {
        super(message);
    }

    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
