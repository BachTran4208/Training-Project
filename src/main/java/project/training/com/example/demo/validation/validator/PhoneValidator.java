package project.training.com.example.demo.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import project.training.com.example.demo.validation.annotation.ValidPhone;

public class PhoneValidator
        implements ConstraintValidator<ValidPhone, String> {

    @Override
    public boolean isValid(String value,
                           ConstraintValidatorContext context) {

        if (value == null) {
            return false;
        }

        return value.matches("\\d{10}");
    }
}
