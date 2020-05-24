package cn.theproudsoul.justwriteit.web.exception;

/**
 * @author TheProudSoul
 */
public class StorageFileNotFoundException extends RuntimeException {

    public StorageFileNotFoundException(String message) {
        super(message);
    }

    public StorageFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
