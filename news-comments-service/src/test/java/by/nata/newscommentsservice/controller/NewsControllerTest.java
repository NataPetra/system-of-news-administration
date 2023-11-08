package by.nata.newscommentsservice.controller;

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
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
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

@WebMvcTest(controllers = NewsController.class)
@MockBean(JpaMetamodelMappingContext.class)
class NewsControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private INewsService newsService;


    @Test
    void saveNews() throws Exception {
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
    void getAllNews() throws Exception {
        int pageNumber = 0;
        int pageSize = 10;
        List<NewsResponseDto> responseList = createNewsResponseDtoList();

        when(newsService.getAllNews(pageNumber, pageSize)).thenReturn(responseList);

        MvcResult mvcResult = mockMvc.perform(get(URL_TEMPLATE_GET_ALL, pageNumber, pageSize))
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
        when(newsService.getNewsWithComments(newsId, pageNumber, pageSize)).thenReturn(response);

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

        when(newsService.searchNews(keyword, dateString, pageNumber, pageSize)).thenReturn(responseList);

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