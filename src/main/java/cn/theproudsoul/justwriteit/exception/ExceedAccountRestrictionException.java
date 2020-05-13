package cn.theproudsoul.justwriteit.exception;

/**
 * @author TheProudSoul
 */
public class ExceedAccountRestrictionException extends RuntimeException {

    public ExceedAccountRestrictionException(String message) {
        super(message);
    }

    public ExceedAccountRestrictionException(String message, Throwable cause) {
        super(message, cause);
    }
}
