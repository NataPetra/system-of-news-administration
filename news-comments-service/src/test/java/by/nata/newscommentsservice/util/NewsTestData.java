package by.nata.newscommentsservice.util;

import by.nata.newscommentsservice.database.model.News;
import by.nata.newscommentsservice.service.dto.NewsRequestDto;
import by.nata.newscommentsservice.service.dto.NewsResponseDto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public final class NewsTestData {

    private NewsTestData() {
    }

    public static News.NewsBuilder createNews() {
        return News.builder();
    }

    public static NewsRequestDto.NewsRequestDtoBuilder createNewsRequestDto() {
        return NewsRequestDto.builder()
                .withTitle("ABC")
                .withText("Very interesting news");
    }

    public static NewsResponseDto.NewsResponseDtoBuilder createNewsResponseDto() {
        return NewsResponseDto.builder()
                .withId(1L)
                .withTime(new Date())
                .withTitle("ABC")
                .withText("Very interesting news");
    }

    public static List<News> createNewsList() {
        List<News> newsList = new ArrayList<>();
        newsList.add(News.builder()
                .withId(1L)
                .withTime(new Date())
                .withTitle("Title1")
                .withText("Text1")
                .build());
        newsList.add(News.builder()
                .withId(2L)
                .withTime(new Date())
                .withTitle("Title2")
                .withText("Text2")
                .build());
        newsList.add(News.builder()
                .withId(3L)
                .withTime(new Date())
                .withTitle("Title3")
                .withText("Text4")
                .build());
        return newsList;
    }

    public static List<NewsResponseDto> createNewsResponseDtoList() {
        List<NewsResponseDto> newsResponseDtoList = new ArrayList<>();
        newsResponseDtoList.add(NewsResponseDto.builder()
                .withId(1L)
                .withTime(new Date())
                .withTitle("Title1")
                .withText("Text1")
                .build());
        newsResponseDtoList.add(NewsResponseDto.builder()
                .withId(2L)
                .withTime(new Date())
                .withTitle("Title2")
                .withText("Text2")
                .build());
        newsResponseDtoList.add(NewsResponseDto.builder()
                .withId(3L)
                .withTime(new Date())
                .withTitle("Title3")
                .withText("Text4")
                .build());
        return newsResponseDtoList;
    }
}
