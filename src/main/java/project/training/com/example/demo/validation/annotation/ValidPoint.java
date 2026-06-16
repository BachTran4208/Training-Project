package project.training.com.example.demo.validation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import project.training.com.example.demo.validation.validator.PointValidator;

@Documented
@Constraint(validatedBy = PointValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPoint {
    String message() default "Point must be one of: 1, 2, 3, 5, 8";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
