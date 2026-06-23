package project.training.com.example.demo.validation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import project.training.com.example.demo.validation.validator.PhoneValidator;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PhoneValidator.class)
@Documented
public @interface ValidPhone {

    String message() default "Phone must contain exactly 10 digits";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
