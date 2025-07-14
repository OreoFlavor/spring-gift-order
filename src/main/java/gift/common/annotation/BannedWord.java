package gift.common.annotation;

import gift.common.validator.BannedWordValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = BannedWordValidator.class)
@Target({ FIELD })
@Retention(RUNTIME)
public @interface BannedWord {
    String[] words();
    String message() default "금지된 단어는 담당 MD와 협의 후 사용 가능합니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
