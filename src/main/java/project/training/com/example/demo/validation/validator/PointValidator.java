package project.training.com.example.demo.validation.validator;

import java.util.Set;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import project.training.com.example.demo.validation.annotation.ValidPoint;

public class PointValidator implements ConstraintValidator<ValidPoint, Integer> {

    private static final Set<Integer> ALLOWED = Set.of(1, 2, 3, 5, 8);

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value == null) return false;
        return ALLOWED.contains(value);
    }
}
