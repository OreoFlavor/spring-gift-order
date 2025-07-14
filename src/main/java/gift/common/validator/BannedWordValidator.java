package gift.common.validator;

import gift.common.annotation.BannedWord;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class BannedWordValidator implements ConstraintValidator<BannedWord, String> {
    private String[] bannedWords;

    @Override
    public void initialize(BannedWord constraintAnnotation) {
        bannedWords = constraintAnnotation.words();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null)
            return true;
        for (String bannedWord : bannedWords) {
            if (value.contains(bannedWord))
                return false;
        }
        return true;
    }
}
