package com.swave.urnr.releasenote.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swave.urnr.releasenote.requestdto.CommentCreateRequestDTO;
import com.swave.urnr.releasenote.responsedto.CommentContentListResponseDTO;
import com.swave.urnr.releasenote.responsedto.CommentContentResponseDTO;
import com.swave.urnr.releasenote.service.CommentService;
import com.swave.urnr.util.http.HttpResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CommentController.class)
class CommentControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mvc;

    @MockBean
    private CommentService commentService;

    @BeforeEach
    void setUp(WebApplicationContext applicationContext) {
        mvc = MockMvcBuilders.webAppContextSetup(applicationContext)
                .addFilter(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true))
                .build();
    }

    @Test
    @DisplayName("댓글 생성 컨트롤러 테스트")
    void createComment() throws Exception {
        HttpResponse httpResponse = HttpResponse.builder()
                .message("Comment Created")
                .description("Comment ID : " + 1L + " Created")
                .build();

        CommentCreateRequestDTO commentCreateRequestDTO = new CommentCreateRequestDTO();
        commentCreateRequestDTO.setContent("test");

        given(commentService.createComment(any(HttpServletRequest.class),eq(1L),eq(commentCreateRequestDTO)))
                .willReturn(httpResponse);

        mvc.perform(post("/api/project/release-note/1/comment")
                        .requestAttr("id", 1L)
                        .requestAttr("username", "Kim")
                        .content(objectMapper.writeValueAsString(commentCreateRequestDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(httpResponse)));
    }

    @Test
    @DisplayName("댓글 로드 컨트롤러 테스트")
    void loadRecentComment() throws Exception {
        CommentContentListResponseDTO commentContentListResponseDTO = new CommentContentListResponseDTO(new ArrayList<>());

        CommentContentResponseDTO commentContentResponseDTO = new CommentContentResponseDTO();
        commentContentResponseDTO.setContext("test");
        commentContentResponseDTO.setName("Kim");
        commentContentResponseDTO.setLastModifiedDate(new Date());

        commentContentListResponseDTO.getComments().add(commentContentResponseDTO);

        given(commentService.loadRecentComment(1L)).willReturn(commentContentListResponseDTO);

        mvc.perform(get("/api/project/1/release-note/recent-comments")
                        .requestAttr("id", 1L)
                        .requestAttr("username", "Kim")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(commentContentListResponseDTO)));
    }

    @Test
    @DisplayName("댓글 삭제 컨트롤러 테스트")
    void deleteComment() throws Exception {
        HttpResponse httpResponse = HttpResponse.builder()
                .message("Comment Deleted")
                .description("Comment ID : " + 1L + " Deleted")
                .build();

        given(commentService.deleteComment(1L)).willReturn(httpResponse);

        mvc.perform(delete("/api/project/release-note/comment/1")
                        .requestAttr("id", 1L)
                        .requestAttr("username", "Kim")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(httpResponse)));
    }
}