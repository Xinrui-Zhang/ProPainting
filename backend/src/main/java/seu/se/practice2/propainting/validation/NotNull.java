package seu.se.practice2.propainting.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
@Inherited
@Constraint(validatedBy = NotNullValidator.class)
public @interface NotNull {

	String message() default "当前属性为null或空!";  // 必须有

	Class<?>[] groups() default {};  // 必须有

	Class<? extends Payload>[] payload() default {};  // 必须有

}
