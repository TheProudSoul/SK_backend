package cn.theproudsoul.sk.web.exception;

/**
 * @author TheProudSoul
 */
@SuppressWarnings("serial")
public class EmailExistsException extends Throwable {

    public EmailExistsException(final String message) {
        super(message);
    }

}
