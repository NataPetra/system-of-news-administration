package by.nata.newscommentsservice.controller;

import by.nata.exceptionhandlingstarter.handler.GlobalExceptionHandlerAdvice;
import by.nata.newscommentsservice.security.filter.AuthenticationJwtFilter;
import by.nata.newscommentsservice.service.api.INewsService;
import by.nata.newscommentsservice.service.dto.NewsRequestDto;
import by.nata.newscommentsservice.service.dto.NewsResponseDto;
import by.nata.newscommentsservice.service.dto.NewsWithCommentsResponseDto;
import by.nata.newscommentsservice.util.CommentTestData;
import by.nata.newscommentsservice.util.NewsTestData;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static by.nata.newscommentsservice.util.NewsTestData.URL_TEMPLATE_GET_ALL;
import static by.nata.newscommentsservice.util.NewsTestData.URL_TEMPLATE_GET_WITH_COMMENT;
import static by.nata.newscommentsservice.util.NewsTestData.URL_TEMPLATE_SAVE;
import static by.nata.newscommentsservice.util.NewsTestData.URL_TEMPLATE_SEARCH;
import static by.nata.newscommentsservice.util.NewsTestData.URL_TEMPLATE_UPDATE_GET_DELETE;
import static by.nata.newscommentsservice.util.NewsTestData.createNewsResponseDtoList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.containsString;

@ActiveProfiles("test")
@WebMvcTest(
        controllers = NewsController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = AuthenticationJwtFilter.class)
)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandlerAdvice.class)
class NewsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private INewsService newsService;


    @Test
    void saveNewsIsSuccessful() throws Exception {
        NewsRequestDto request = NewsTestData.createNewsRequestDto().build();
        NewsResponseDto response = NewsTestData.createNewsResponseDto().build();

        when(newsService.save(request)).thenReturn(response);

        mockMvc.perform(post(URL_TEMPLATE_SAVE)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().string(objectMapper.writeValueAsString(response)));
    }

    @Test
    void saveNewsIsUnsuccessfulWithEmptyFieldText() throws Exception {
        NewsRequestDto request = NewsRequestDto.builder().withTitle("ABC").build();

        mockMvc.perform(post(URL_TEMPLATE_SAVE)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Text must not be null or empty")));
    }

    @Test
    void saveNewsIsUnsuccessfulWithEmptyFieldTitle() throws Exception {
        NewsRequestDto request = NewsRequestDto.builder().withText("Text").build();

        mockMvc.perform(post(URL_TEMPLATE_SAVE)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Title must not be null or empty")));
    }

    @Test
    void saveNewsIsUnsuccessfulWhenNewsIsNull() throws Exception {
        mockMvc.perform(post(URL_TEMPLATE_SAVE)
                        .content(objectMapper.writeValueAsString(null))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Malformed JSON request")));
    }

    @Test
    void updateNews() throws Exception {
        Long newsId = 1L;
        NewsRequestDto request = NewsTestData.createNewsRequestDto().build();
        NewsResponseDto response = NewsTestData.createNewsResponseDto().build();

        when(newsService.isNewsExist(newsId)).thenReturn(true);
        when(newsService.update(newsId, request)).thenReturn(response);

        mockMvc.perform(put(URL_TEMPLATE_UPDATE_GET_DELETE, newsId)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(response)));
    }

    @Test
    void updateNewsIsUnsuccessfulWhenNewsIsNull() throws Exception {
        Long newsId = 1L;

        when(newsService.isNewsExist(newsId)).thenReturn(true);

        mockMvc.perform(put(URL_TEMPLATE_UPDATE_GET_DELETE, newsId)
                        .content(objectMapper.writeValueAsString(null))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Malformed JSON request")));
    }

    @Test
    void updateNewsIsUnsuccessfulWithEmptyFieldText() throws Exception {
        Long newsId = 1L;

        when(newsService.isNewsExist(newsId)).thenReturn(true);

        NewsRequestDto request = NewsRequestDto.builder().withTitle("ABC").build();

        mockMvc.perform(put(URL_TEMPLATE_UPDATE_GET_DELETE, newsId)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Text must not be null or empty")));
    }

    @Test
    void updateNewsIsUnsuccessfulWithEmptyFieldTitle() throws Exception {
        Long newsId = 1L;

        when(newsService.isNewsExist(newsId)).thenReturn(true);

        NewsRequestDto request = NewsRequestDto.builder().withText("Text").build();

        mockMvc.perform(put(URL_TEMPLATE_UPDATE_GET_DELETE, newsId)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Title must not be null or empty")));
    }

    @Test
    void getNews() throws Exception {
        Long newsId = 1L;
        NewsResponseDto response = NewsTestData.createNewsResponseDto().build();

        when(newsService.isNewsExist(newsId)).thenReturn(true);
        when(newsService.getNewsById(newsId)).thenReturn(response);

        mockMvc.perform(get(URL_TEMPLATE_UPDATE_GET_DELETE, newsId))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(response)));
    }

    @Test
    void getNewsWhenInvalidNewsId() throws Exception {
        Long newsId = 1L;
        when(newsService.isNewsExist(newsId)).thenReturn(false);

        mockMvc.perform(get(URL_TEMPLATE_UPDATE_GET_DELETE, newsId))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Invalid id: news not found\":\"id")));
    }

    @Test
    void getAllNews() throws Exception {
        int pageNumber = 0;
        int pageSize = 10;
        List<NewsResponseDto> responseList = createNewsResponseDtoList();

        when(newsService.getAllNews(PageRequest.of(pageNumber, pageSize))).thenReturn(responseList);

        MvcResult mvcResult = mockMvc.perform(get(URL_TEMPLATE_GET_ALL, pageNumber, pageSize)
                        .param("page", String.valueOf(pageNumber))
                        .param("size", String.valueOf(pageSize)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();
        List<NewsResponseDto> result = objectMapper.readValue(responseBody, new TypeReference<>() {
        });

        assertEquals("Title1", result.get(0).title());
        org.assertj.core.api.Assertions.assertThat(responseBody).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(responseList));

    }

    @Test
    void getNewsWithComments() throws Exception {
        Long newsId = 1L;
        int pageNumber = 0;
        int pageSize = 10;
        NewsWithCommentsResponseDto response = NewsWithCommentsResponseDto.builder()
                .withId(newsId)
                .withTitle("A")
                .withText("B")
                .withCommentsList(CommentTestData.createCommentResponseDtoList())
                .withTime("2023-11-03 18:56:11")
                .build();

        when(newsService.isNewsExist(newsId)).thenReturn(true);
        when(newsService.getNewsWithComments(newsId, PageRequest.of(pageNumber, pageSize))).thenReturn(response);

        mockMvc.perform(get(URL_TEMPLATE_GET_WITH_COMMENT, newsId, pageNumber, pageSize))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(response)));
    }

    @Test
    void searchNews() throws Exception {
        String keyword = "Title";
        String dateString = "2023-10-31";
        int pageNumber = 0;
        int pageSize = 10;
        List<NewsResponseDto> responseList = createNewsResponseDtoList();

        when(newsService.searchNews(keyword, dateString, PageRequest.of(pageNumber, pageSize, Sort.unsorted()))).thenReturn(responseList);

        mockMvc.perform(get(URL_TEMPLATE_SEARCH, keyword, dateString, pageNumber, pageSize))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(responseList)));
    }

    @Test
    void deleteNews() throws Exception {
        Long newsId = 1L;

        when(newsService.isNewsExist(newsId)).thenReturn(true);
        mockMvc.perform(delete(URL_TEMPLATE_UPDATE_GET_DELETE, newsId))
                .andExpect(status().isNoContent());

        verify(newsService).delete(newsId);
    }
}