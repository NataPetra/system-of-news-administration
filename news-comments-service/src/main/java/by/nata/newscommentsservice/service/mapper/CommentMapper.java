package by.nata.newscommentsservice.service.mapper;

import by.nata.newscommentsservice.database.model.Comment;
import by.nata.newscommentsservice.database.model.News;
import by.nata.newscommentsservice.database.repository.NewsRepository;
import by.nata.newscommentsservice.service.dto.CommentRequestDto;
import by.nata.newscommentsservice.service.dto.CommentResponseDto;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The {@code CommentMapper} class provides mapping functions for converting between Comment and CommentRequestDto/CommentResponseDto objects.
 *
 * <p>Usage:</p>
 * <p>- Use this mapper to convert CommentRequestDto objects to Comment entities and vice versa, as well as to convert Comment entities to CommentResponseDto objects.</p>
 *
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        builder = @Builder(disableBuilder = true))
public abstract class CommentMapper {

    @Autowired
    protected NewsRepository newsRepository;

    /**
     * Converts a CommentRequestDto object to a Comment entity.
     *
     * @param commentRequestDto The CommentRequestDto to be converted.
     * @return The Comment entity with relevant properties set. Ignores 'id' and 'time' properties and sets the 'news' property based on the 'newsId' from the CommentRequestDto.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "time", ignore = true)
    @Mapping(target = "news", expression = "java(getNewsById(commentRequestDto.newsId()))")
    public abstract Comment dtoToEntity(CommentRequestDto commentRequestDto);

    /**
     * Converts a Comment entity to a CommentResponseDto.
     *
     * @param comment The Comment entity to be converted.
     * @return The CommentResponseDto with 'newsId' mapped from the associated News entity and 'time' property formatted to a string.
     */
    @Mapping(target = "newsId", source = "comment.news.id")
    @Mapping(target = "time", expression = "java(formatDate(comment.getTime()))")
    public abstract CommentResponseDto entityToDto(Comment comment);

    /**
     * Retrieves a News entity by its unique identifier.
     *
     * @param id The unique identifier of the News entity.
     * @return The associated News entity if found, or null if not found.
     */
    public News getNewsById(Long id) {
        return newsRepository.findById(id)
                .orElse(null);
    }

    /**
     * Formats a Date object to a string with the pattern "yyyy-MM-dd HH:mm:ss".
     *
     * @param date The Date object to be formatted.
     * @return The formatted date as a string.
     */
    public String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }
}
