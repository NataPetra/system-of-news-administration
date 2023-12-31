package by.nata.newscommentsservice.controller;

import by.nata.exceptionhandlingstarter.handler.GlobalExceptionHandlerAdvice;
import by.nata.newscommentsservice.security.filter.AuthenticationJwtFilter;
import by.nata.newscommentsservice.service.api.ICommentService;
import by.nata.newscommentsservice.service.api.INewsService;
import by.nata.newscommentsservice.service.dto.CommentRequestDto;
import by.nata.newscommentsservice.service.dto.CommentResponseDto;
import by.nata.newscommentsservice.util.CommentTestData;
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

import static by.nata.newscommentsservice.util.CommentTestData.URL_TEMPLATE_GET_BY_NEWS_ID;
import static by.nata.newscommentsservice.util.CommentTestData.URL_TEMPLATE_SAVE;
import static by.nata.newscommentsservice.util.CommentTestData.URL_TEMPLATE_SEARCH;
import static by.nata.newscommentsservice.util.CommentTestData.URL_TEMPLATE_UPDATE_GET_DELETE;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(
        controllers = CommentController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = AuthenticationJwtFilter.class)
)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandlerAdvice.class)
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ICommentService commentService;

    @MockBean
    private INewsService newsService;

    public static final Long COMMENT_ID = 1L;

    @Test
    void saveComment() throws Exception {
        CommentRequestDto request = CommentTestData.createCommentRequestDto().build();
        CommentResponseDto response = CommentTestData.createCommentResponseDto().build();

        when(newsService.isNewsExist(1L)).thenReturn(true);
        when(commentService.save(request)).thenReturn(response);

        mockMvc.perform(post(URL_TEMPLATE_SAVE)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().string(objectMapper.writeValueAsString(response)));
    }

    @Test
    void saveCommentIsUnsuccessfulWithEmptyFieldText() throws Exception {
        CommentRequestDto request = CommentRequestDto.builder().withNewsId(1L).build();

        when(newsService.isNewsExist(1L)).thenReturn(true);

        mockMvc.perform(post(URL_TEMPLATE_SAVE)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Text must not be null or empty")));
    }

    @Test
    void saveCommentIsUnsuccessfulWhenNewsIdIsNull() throws Exception {
        CommentRequestDto request = CommentRequestDto.builder().withText("Text").withNewsId(null).build();

        mockMvc.perform(post(URL_TEMPLATE_SAVE)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("News id must not be null")));
    }

    @Test
    void saveCommentIsUnsuccessfulWhenNewsIdIsNotExist() throws Exception {
        CommentRequestDto request = CommentTestData.createCommentRequestDto().build();

        when(newsService.isNewsExist(1L)).thenReturn(false);

        mockMvc.perform(post(URL_TEMPLATE_SAVE)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Invalid id: news not found")));
    }

    @Test
    void updateComment() throws Exception {
        CommentRequestDto request = CommentTestData.createCommentRequestDto().build();
        CommentResponseDto response = CommentTestData.createCommentResponseDto().build();

        when(commentService.isCommentExist(COMMENT_ID)).thenReturn(true);
        when(newsService.isNewsExist(1L)).thenReturn(true);
        when(commentService.update(COMMENT_ID, request)).thenReturn(response);

        mockMvc.perform(put(URL_TEMPLATE_UPDATE_GET_DELETE, COMMENT_ID)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(response)));
    }

    @Test
    void updateCommentIsUnsuccessfulWhenCommentIdIsNotExist() throws Exception {
        CommentRequestDto request = CommentTestData.createCommentRequestDto().build();

        when(commentService.isCommentExist(COMMENT_ID)).thenReturn(false);
        when(newsService.isNewsExist(1L)).thenReturn(true);

        mockMvc.perform(put(URL_TEMPLATE_UPDATE_GET_DELETE, COMMENT_ID)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Invalid id: comment not found\":\"id")));
    }

    @Test
    void getComment() throws Exception {
        CommentResponseDto response = CommentTestData.createCommentResponseDto().build();

        when(commentService.isCommentExist(COMMENT_ID)).thenReturn(true);
        when(commentService.getCommentById(COMMENT_ID)).thenReturn(response);

        mockMvc.perform(get(URL_TEMPLATE_UPDATE_GET_DELETE, COMMENT_ID))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(response)));
    }

    @Test
    void getCommentsByNewsId() throws Exception {
        Long newsId = 1L;
        List<CommentResponseDto> responseList = CommentTestData.createCommentResponseDtoList();

        when(newsService.isNewsExist(newsId)).thenReturn(true);
        when(commentService.findAllByNewsId(newsId)).thenReturn(responseList);

        MvcResult mvcResult = mockMvc.perform(get(URL_TEMPLATE_GET_BY_NEWS_ID, newsId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();
        List<CommentResponseDto> result = objectMapper.readValue(responseBody, new TypeReference<>() {
        });

        assertEquals("Comment1 test", result.get(0).text());
        org.assertj.core.api.Assertions.assertThat(responseBody).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(responseList));
    }

    @Test
    void searchComments() throws Exception {
        String keyword = "test";
        int pageNumber = 0;
        int pageSize = 10;
        List<CommentResponseDto> responseList = CommentTestData.createCommentResponseDtoList();

        when(commentService.searchComment(keyword, PageRequest.of(pageNumber, pageSize, Sort.unsorted()))).thenReturn(responseList);

        mockMvc.perform(get(URL_TEMPLATE_SEARCH, keyword, pageNumber, pageSize))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(responseList)));
    }

    @Test
    void deleteComment() throws Exception {
        Long commentId = 1L;

        when(commentService.isCommentExist(COMMENT_ID)).thenReturn(true);
        mockMvc.perform(delete(URL_TEMPLATE_UPDATE_GET_DELETE, commentId))
                .andExpect(status().isNoContent());

        verify(commentService).delete(commentId);
    }
}