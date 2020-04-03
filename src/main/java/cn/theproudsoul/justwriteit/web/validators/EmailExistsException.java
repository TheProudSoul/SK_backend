package cn.theproudsoul.justwriteit.web.validators;

/**
 * @author TheProudSoul
 */
@SuppressWarnings("serial")
public class EmailExistsException extends Throwable {

    public EmailExistsException(final String message) {
        super(message);
    }

}
