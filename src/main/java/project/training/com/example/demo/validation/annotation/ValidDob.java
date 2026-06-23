package project.training.com.example.demo.validation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import project.training.com.example.demo.validation.validator.DobValidator;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DobValidator.class)
@Documented
public @interface ValidDob {

    String message() default "DOB must be in MM/dd/yyyy format";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
