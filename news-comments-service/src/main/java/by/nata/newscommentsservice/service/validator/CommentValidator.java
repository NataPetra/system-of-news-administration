package by.nata.newscommentsservice.service.validator;

import by.nata.newscommentsservice.service.api.ICommentService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

/**
 * A custom constraint validator for the {@link CommentValidation} annotation.
 * It checks if a comment with the provided ID exists in the system.
 */
@RequiredArgsConstructor
public class CommentValidator implements ConstraintValidator<CommentValidation, Long> {

    private final ICommentService commentService;

    /**
     * Validates whether a comment with the specified ID exists in the system.
     *
     * @param value    The comment ID to be validated.
     * @param context  The validation context.
     * @return true if the comment with the given ID exists, false otherwise.
     */
    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return commentService.isCommentExist(value);
    }
}
