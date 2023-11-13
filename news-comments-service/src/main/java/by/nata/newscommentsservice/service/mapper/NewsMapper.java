package by.nata.newscommentsservice.service.mapper;

import by.nata.newscommentsservice.database.model.News;
import by.nata.newscommentsservice.service.dto.NewsRequestDto;
import by.nata.newscommentsservice.service.dto.NewsResponseDto;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Mapper interface for converting between NewsRequestDto, News entity, and NewsResponseDto.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        builder = @Builder(disableBuilder = true))
public interface NewsMapper {

    /**
     * Converts a NewsRequestDto object to a News entity.
     *
     * @param newsRequestDto The NewsRequestDto to be converted.
     * @return The News entity with relevant properties set. Ignores 'id', 'time', and 'comments' properties.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "time", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "username", expression = "java(getUsernameFromSecurityContext())")
    News dtoToEntity(NewsRequestDto newsRequestDto);

    /**
     * Converts a News entity to a NewsResponseDto.
     *
     * @param news The News entity to be converted.
     * @return The NewsResponseDto with the 'time' property formatted as a string.
     */
    @Mapping(target = "time", expression = "java(formatDate(news.getTime()))")
    NewsResponseDto entityToDto(News news);

    /**
     * Formats a Date object to a string with the pattern "yyyy-MM-dd HH:mm:ss".
     *
     * @param date The Date object to be formatted.
     * @return The formatted date as a string.
     */
    default String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    default String getUsernameFromSecurityContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null ? authentication.getName() : null;
    }
}
