package by.nata.newscommentsservice.service.mapper;

import by.nata.newscommentsservice.database.model.News;
import by.nata.newscommentsservice.service.dto.NewsRequestDto;
import by.nata.newscommentsservice.service.dto.NewsResponseDto;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        builder = @Builder(disableBuilder = true))
public interface NewsMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "time", ignore = true)
    @Mapping(target = "comments", ignore = true)
    News dtoToEntity(NewsRequestDto newsRequestDto);

    NewsResponseDto entityToDto(News news);
}
