package by.nata.newscommentsservice.service.validator;

import by.nata.newscommentsservice.service.api.INewsService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NewsValidator implements ConstraintValidator<NewsValidation, Long> {

    private final INewsService newsService;

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return newsService.isNewsExist(value);
    }
}
