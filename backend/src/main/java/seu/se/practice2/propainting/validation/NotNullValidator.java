package seu.se.practice2.propainting.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import seu.se.practice2.propainting.util.ValidationUtil;

public class NotNullValidator implements ConstraintValidator<NotNull, Object> {
	@Override
	public boolean isValid(
		Object o, ConstraintValidatorContext constraintValidatorContext
	) {
		return ValidationUtil.isNotNull(o);
	}
}
