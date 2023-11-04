package by.nata.newscommentsservice.service;

import by.nata.newscommentsservice.service.api.ICommentService;
import by.nata.newscommentsservice.service.dto.CommentResponseDto;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertSame;

@ActiveProfiles("dev")
@SpringBootTest
@RunWith(SpringRunner.class)
class TestServ {

    @Autowired
    ICommentService commentService;

    @Test
    void test()
    {
        CommentResponseDto commentById = commentService.getCommentById(1L);

        CommentResponseDto commentById1 = commentService.getCommentById(1L);

        assertSame(commentById, commentById1);


    }
}
