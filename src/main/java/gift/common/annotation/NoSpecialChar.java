package gift.common.annotation;

import gift.common.validator.NoSpecialCharValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = NoSpecialCharValidator.class)
@Target({ FIELD })
@Retention(RUNTIME)
public @interface NoSpecialChar {
    String message() default "알파벳, 한글, 숫자, (, ), [, ], +, -, &, /, _ 만 입력 가능합니다";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
