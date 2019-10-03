package gov.kui.docRepoR.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueValueValidator.class)
public @interface UniqueValue {
    String message() default "The value must be unique";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    Class<? extends CheckValueIsExists> service();
    String fieldName();
}