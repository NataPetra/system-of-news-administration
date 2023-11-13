package by.nata.newscommentsservice.service.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;

/**
 * Custom validation annotation for validating the existence of comment by its ID.
 * It can be applied to fields and method parameters.
 */
@Target({FIELD, PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = CommentValidator.class)
public @interface CommentValidation {

    String message() default "Invalid id: comment not found";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
