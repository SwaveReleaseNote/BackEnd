package com.swave.urnr.releasenote.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swave.urnr.releasenote.requestdto.CommentCreateRequestDTO;
import com.swave.urnr.releasenote.responsedto.ReleaseNoteLabelContentResponseDTO;
import com.swave.urnr.releasenote.responsedto.ReleaseNoteLabelCountResponseDTO;
import com.swave.urnr.releasenote.service.CommentService;
import com.swave.urnr.releasenote.service.NoteBlockService;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = NoteBlockController.class)
class NoteBlockControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mvc;

    @MockBean
    private NoteBlockService noteBlockService;

    @BeforeEach
    void setUp(WebApplicationContext applicationContext) {
        mvc = MockMvcBuilders.webAppContextSetup(applicationContext)
                .addFilter(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true))
                .build();
    }

    @Test
    @DisplayName("라벨 카운트 컨트롤러 테스트")
    void countLabel() throws Exception {
        List<ReleaseNoteLabelCountResponseDTO> releaseNoteLabelCountResponseDTOList = new ArrayList<>();

        ReleaseNoteLabelCountResponseDTO releaseNoteLabelCountResponseDTO = new ReleaseNoteLabelCountResponseDTO();
        releaseNoteLabelCountResponseDTO.setLabel("new");
        releaseNoteLabelCountResponseDTO.setCount(2L);

        releaseNoteLabelCountResponseDTOList.add(releaseNoteLabelCountResponseDTO);

        given(noteBlockService.countLabel(1L))
                .willReturn(releaseNoteLabelCountResponseDTOList);

        mvc.perform(get("/api/project/1/release-note/label/count")
                        .requestAttr("id", 1L)
                        .requestAttr("username", "Kim")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(releaseNoteLabelCountResponseDTOList)));
    }

    @Test
    @DisplayName("라벨 필터링 컨트롤러 테스트")
    void filterByLabel() throws Exception {
        List<ReleaseNoteLabelContentResponseDTO> releaseNoteLabelContentResponseDTOList = new ArrayList<>();

        ReleaseNoteLabelContentResponseDTO releaseNoteLabelContentResponseDTO = new ReleaseNoteLabelContentResponseDTO();
        releaseNoteLabelContentResponseDTO.setReleaseNoteId(1L);
        releaseNoteLabelContentResponseDTO.setVersion("1.0.0");
        releaseNoteLabelContentResponseDTO.setContext(List.of("test"));
        releaseNoteLabelContentResponseDTO.setLabel("new");

        releaseNoteLabelContentResponseDTOList.add(releaseNoteLabelContentResponseDTO);

        given(noteBlockService.filterByLabel(1L))
                .willReturn(releaseNoteLabelContentResponseDTOList);

        mvc.perform(get("/api/project/1/release-note/label/filter")
                        .requestAttr("id", 1L)
                        .requestAttr("username", "Kim")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(releaseNoteLabelContentResponseDTOList)));
    }
}