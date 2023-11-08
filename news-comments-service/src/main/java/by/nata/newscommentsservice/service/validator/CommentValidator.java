package by.nata.newscommentsservice.service.validator;

import by.nata.newscommentsservice.service.api.ICommentService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CommentValidator implements ConstraintValidator<CommentValidation, Long> {

    private final ICommentService commentService;

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return commentService.isCommentExist(value);
    }
}
