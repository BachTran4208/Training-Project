package project.training.com.example.demo.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import project.training.com.example.demo.validation.annotation.NullableNotBlank;

public class NullableNotBlankValidator
        implements ConstraintValidator<NullableNotBlank, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (value == null) {
            return true;
        }

        return !value.trim().isEmpty();
    }
}
