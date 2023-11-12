package by.nata.newscommentsservice.util;

import by.nata.newscommentsservice.database.model.News;
import by.nata.newscommentsservice.service.dto.CommentResponseDto;
import by.nata.newscommentsservice.service.dto.NewsRequestDto;
import by.nata.newscommentsservice.service.dto.NewsResponseDto;
import by.nata.newscommentsservice.service.dto.NewsWithCommentsResponseDto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static by.nata.newscommentsservice.util.DateFormater.formatDate;

public final class NewsTestData {

    public static final String URL_TEMPLATE_SAVE = "/api/v1/app/news/";
    public static final String URL_TEMPLATE_UPDATE_GET_DELETE = "/api/v1/app/news/{id}";
    public static final String URL_TEMPLATE_GET_ALL = "/api/v1/app/news/?pageNumber={pageNumber}&pageSize={pageSize}";
    public static final String URL_TEMPLATE_GET_WITH_COMMENT = "/api/v1/app/news/{newsId}/comments?pageNumber={pageNumber}&pageSize={pageSize}";
    public static final String URL_TEMPLATE_SEARCH = "/api/v1/app/news/search?keyword={keyword}&dateString={dateString}&pageNumber={pageNumber}&pageSize={pageSize}";

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
                .withTime("2023-11-03 18:56:11")
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
                .withTime("2023-11-03 18:56:11")
                .withTitle("Title1")
                .withText("Text1")
                .build());
        newsResponseDtoList.add(NewsResponseDto.builder()
                .withId(2L)
                .withTime("2023-11-03 18:56:11")
                .withTitle("Title2")
                .withText("Text2")
                .build());
        newsResponseDtoList.add(NewsResponseDto.builder()
                .withId(3L)
                .withTime("2023-11-03 18:56:11")
                .withTitle("Title3")
                .withText("Text4")
                .build());
        return newsResponseDtoList;
    }

    public static NewsRequestDto createNewsRequestDtoIntegr() {
        return NewsRequestDto.builder()
                .withTitle("New News")
                .withText("This is a new test news")
                .build();
    }

    public static NewsResponseDto createExpectedNewsResponseDtoIntegr() {
        return NewsResponseDto.builder()
                .withId(1L)
                .withTitle("New News")
                .withText("This is a new test news")
                .withTime(formatDate(new Date()))
                .build();
    }

    public static NewsResponseDto createExpectedNewsResponseDtoForUpdateIntegr() {
        return NewsResponseDto.builder()
                .withId(1L)
                .withTitle("New News")
                .withText("This is a new test news")
                .withTime("2023-11-03 01:46:22")
                .build();
    }

    public static NewsWithCommentsResponseDto createExpectedNewsWithCommentsResponseDtoIntegr() {
        return NewsWithCommentsResponseDto.builder()
                .withId(1L)
                .withTitle("News 1")
                .withText("This is a test news 1")
                .withTime("2023-11-03 01:46:22")
                .withCommentsList(createExpectedCommentsListIntegr())
                .build();
    }

    public static List<CommentResponseDto> createExpectedCommentsListIntegr() {
        List<CommentResponseDto> comments = new ArrayList<>();
        comments.add(CommentResponseDto.builder()
                .withId(1L)
                .withText("Comment 1 for News 1")
                .withUsername("subscriber")
                .withTime("2023-11-03 01:46:22")
                .withNewsId(1L)
                .build());
        comments.add(CommentResponseDto.builder()
                .withId(2L)
                .withText("Comment 2 for News 1")
                .withUsername("subscriber2")
                .withTime("2023-11-02 21:00:24")
                .withNewsId(1L)
                .build());
        return comments;
    }

    public static NewsResponseDto getNewsResponseDtoIntegr() {
        return NewsResponseDto.builder()
                .withId(1L)
                .withTime("2023-11-03 01:46:22")
                .withTitle("News 1")
                .withText("This is a test news 1")
                .build();
    }

    public static List<NewsResponseDto> createExpectedNewsListIntegr() {
        List<NewsResponseDto> newsList = new ArrayList<>();
        newsList.add(getNewsResponseDtoIntegr());
        newsList.add(NewsResponseDto.builder()
                .withId(2L)
                .withTime("2023-11-03 01:46:22")
                .withTitle("News 2")
                .withText("This is a test news 2")
                .build());
        return newsList;
    }
}
