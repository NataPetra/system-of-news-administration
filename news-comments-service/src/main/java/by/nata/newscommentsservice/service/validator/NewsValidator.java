package by.nata.newscommentsservice.service.validator;

import by.nata.newscommentsservice.service.api.INewsService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

/**
 * A custom validator for the {@link NewsValidation} annotation.
 * This validator checks if news with the specified ID exists in the system.
 */
@RequiredArgsConstructor
public class NewsValidator implements ConstraintValidator<NewsValidation, Long> {

    private final INewsService newsService;

    /**
     * Validates if news with the provided ID exists.
     *
     * @param value    The news ID to be validated.
     * @param context  The validation context.
     * @return         True if the news exists or if the value is null; otherwise, false.
     */
    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return newsService.isNewsExist(value);
    }
}
