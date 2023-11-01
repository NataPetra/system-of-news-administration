package by.nata.newscommentsservice.service.mapper;

import by.nata.newscommentsservice.database.model.Comment;
import by.nata.newscommentsservice.service.dto.CommentRequestDto;
import by.nata.newscommentsservice.service.dto.CommentResponseDto;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        builder = @Builder(disableBuilder = true))
public interface CommentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "time", ignore = true)
    Comment dtoToEntity(CommentRequestDto commentRequestDto);

    CommentResponseDto entityToDto(Comment comment);
}
