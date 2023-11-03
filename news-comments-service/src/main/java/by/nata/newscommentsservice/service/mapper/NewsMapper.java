package by.nata.newscommentsservice.service.mapper;

import by.nata.newscommentsservice.database.model.News;
import by.nata.newscommentsservice.service.dto.NewsRequestDto;
import by.nata.newscommentsservice.service.dto.NewsResponseDto;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.text.SimpleDateFormat;
import java.util.Date;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        builder = @Builder(disableBuilder = true))
public interface NewsMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "time", ignore = true)
    @Mapping(target = "comments", ignore = true)
    News dtoToEntity(NewsRequestDto newsRequestDto);

    @Mapping(target = "time", expression = "java(formatDate(news.getTime()))")
    NewsResponseDto entityToDto(News news);

    default String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }
}
