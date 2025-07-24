package gift.common.validator;

import gift.common.annotation.NoSpecialChar;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class NoSpecialCharValidator implements ConstraintValidator<NoSpecialChar, String> {
    private static final Pattern SPECIAL_CHAR_PATTERN = Pattern.compile("^[a-zA-Z가-힣0-9()\\[\\]\\+\\-&/_ ]*$");

    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null || SPECIAL_CHAR_PATTERN.matcher(value).find();
    }
}
