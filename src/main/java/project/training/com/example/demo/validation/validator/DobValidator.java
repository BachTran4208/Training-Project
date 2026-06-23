package project.training.com.example.demo.validation.validator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import project.training.com.example.demo.validation.annotation.ValidDob;

public class DobValidator
        implements ConstraintValidator<ValidDob, String> {
    
    private static final DateTimeFormatter FORMATTER =
        DateTimeFormatter.ofPattern("MM-dd-uuuu")
                .withResolverStyle(java.time.format.ResolverStyle.STRICT);

    @Override
    public boolean isValid(String value,
                           ConstraintValidatorContext context) {


        if (value == null || value.isBlank()) {
            return false;
        }

        try {
            LocalDate.parse(value, FORMATTER);
            return true;
        } catch (Exception e) {
            System.out.print(e.getMessage());
            return false;
        }
    }
}
