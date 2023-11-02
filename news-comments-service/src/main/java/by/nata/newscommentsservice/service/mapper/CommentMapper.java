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

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        builder = @Builder(disableBuilder = true))
public abstract class CommentMapper {

    @Autowired
    protected NewsRepository newsRepository;

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "time", ignore = true)
    @Mapping(target = "news", expression = "java(getNewsById(commentRequestDto.newsId()))")
    public abstract Comment dtoToEntity(CommentRequestDto commentRequestDto);

    @Mapping(target = "newsId", source = "comment.news.id")
    public abstract CommentResponseDto entityToDto(Comment comment);

    public News getNewsById(Long id) {
        return newsRepository.findById(id)
                .orElse(null);
    }
}
