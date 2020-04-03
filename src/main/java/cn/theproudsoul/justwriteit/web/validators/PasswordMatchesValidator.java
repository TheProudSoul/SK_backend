package cn.theproudsoul.justwriteit.web.validators;


import cn.theproudsoul.justwriteit.web.vo.UserRegistrationVo;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author ZHENGLA
 */
public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {
    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
    }
    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context){
        UserRegistrationVo user = (UserRegistrationVo) obj;
        return user.getPassword().equals(user.getConfirmPassword());
    }
}
