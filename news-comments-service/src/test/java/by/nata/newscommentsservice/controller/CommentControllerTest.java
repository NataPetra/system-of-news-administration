package by.nata.newscommentsservice.controller;

import by.nata.newscommentsservice.service.api.ICommentService;
import by.nata.newscommentsservice.service.dto.CommentRequestDto;
import by.nata.newscommentsservice.service.dto.CommentResponseDto;
import by.nata.newscommentsservice.util.CommentTestData;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CommentController.class)
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ICommentService commentService;

    public static final String URL_TEMPLATE_SAVE = "/api/v1/app/comments/";
    public static final String URL_TEMPLATE_UPDATE_GET_DELETE = "/api/v1/app/comments/{id}";
    public static final String URL_TEMPLATE_GET_BY_NEWS_ID = "/api/v1/app/comments/news/{newsId}";
    public static final String URL_TEMPLATE_SEARCH = "/api/v1/app/comments/search?keyword={keyword}&pageNumber={pageNumber}&pageSize={pageSize}";

    @Test
    void saveComment() throws Exception {
        CommentRequestDto request = CommentTestData.createCommentRequestDto().build();
        CommentResponseDto response = CommentTestData.createCommentResponseDto().build();

        when(commentService.save(request)).thenReturn(response);

        mockMvc.perform(post(URL_TEMPLATE_SAVE)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().string(objectMapper.writeValueAsString(response)));
    }

    @Test
    void updateComment() throws Exception {
        Long commentId = 1L;
        CommentRequestDto request = CommentTestData.createCommentRequestDto().build();
        CommentResponseDto response = CommentTestData.createCommentResponseDto().build();

        when(commentService.update(commentId, request)).thenReturn(response);

        mockMvc.perform(put(URL_TEMPLATE_UPDATE_GET_DELETE, commentId)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(response)));
    }

    @Test
    void getComment() throws Exception {
        Long commentId = 1L;
        CommentResponseDto response = CommentTestData.createCommentResponseDto().build();

        when(commentService.getCommentById(commentId)).thenReturn(response);

        mockMvc.perform(get(URL_TEMPLATE_UPDATE_GET_DELETE, commentId))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(response)));
    }

    @Test
    void getCommentsByNewsId() throws Exception {
        Long newsId = 1L;
        List<CommentResponseDto> responseList = CommentTestData.createCommentResponseDtoList();

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

        when(commentService.searchComment(keyword, pageNumber, pageSize)).thenReturn(responseList);

        mockMvc.perform(get(URL_TEMPLATE_SEARCH, keyword, pageNumber, pageSize))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(responseList)));
    }

    @Test
    void deleteComment() throws Exception {
        Long commentId = 1L;

        mockMvc.perform(delete(URL_TEMPLATE_UPDATE_GET_DELETE, commentId))
                .andExpect(status().isNoContent());

        verify(commentService).delete(commentId);
    }
}