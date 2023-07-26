package com.swave.urnr.releasenote.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swave.urnr.releasenote.requestdto.*;
import com.swave.urnr.releasenote.responsedto.ReleaseNoteContentListResponseDTO;
import com.swave.urnr.releasenote.responsedto.ReleaseNoteContentResponseDTO;
import com.swave.urnr.releasenote.responsedto.ReleaseNoteVersionListResponseDTO;
import com.swave.urnr.releasenote.service.ReleaseNoteService;
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
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ReleaseNoteController.class)
class ReleaseNoteControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mvc;

    @MockBean
    private ReleaseNoteService releaseNoteService;

    @BeforeEach
    void setUp(WebApplicationContext applicationContext) {
        mvc = MockMvcBuilders.webAppContextSetup(applicationContext)
                .addFilter(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true))
                .build();
    }

    @Test
    @DisplayName("릴리즈 노트 생성 컨트롤러 테스트")
    void createReleaseNote() throws Exception {
        HttpResponse httpResponse = HttpResponse.builder()
                .message("Release Note Created")
                .description("Release Note ID : " + 1L + " Created")
                .build();

        List<BlockContextCreateRequestDTO> blockContextContentResponseDTOList = new ArrayList<>();
        List<NoteBlockCreateRequestDTO> noteBlockCreateRequestDTOList = new ArrayList<>();

        BlockContextCreateRequestDTO blockContextCreateRequestDTO = new BlockContextCreateRequestDTO();
        blockContextCreateRequestDTO.setContext("test");
        blockContextCreateRequestDTO.setTag("H1");
        blockContextCreateRequestDTO.setIndex(1L);

        blockContextContentResponseDTOList.add(blockContextCreateRequestDTO);

        NoteBlockCreateRequestDTO noteBlockCreateRequestDTO = new NoteBlockCreateRequestDTO();
        noteBlockCreateRequestDTO.setLabel("new");
        noteBlockCreateRequestDTO.setContexts(blockContextContentResponseDTOList);

        noteBlockCreateRequestDTOList.add(noteBlockCreateRequestDTO);

        ReleaseNoteCreateRequestDTO releaseNoteCreateRequestDTO = new ReleaseNoteCreateRequestDTO();
        releaseNoteCreateRequestDTO.setBlocks(noteBlockCreateRequestDTOList);
        releaseNoteCreateRequestDTO.setVersion("1.0.0");
        releaseNoteCreateRequestDTO.setReleaseDate(new Date());

        given(releaseNoteService.createReleaseNote(any(HttpServletRequest.class),eq(1L),any(ReleaseNoteCreateRequestDTO.class)))
                .willReturn(httpResponse);

        mvc.perform(post("/api/project/1/release-note")
                        .requestAttr("id", 1L)
                        .requestAttr("username", "Kim")
                        .content(objectMapper.writeValueAsString(releaseNoteCreateRequestDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(httpResponse)));
    }

    @Test
    @DisplayName("릴리즈 노트 업데이트 컨트롤러 테스트")
    void updateReleaseNote() throws Exception {
        HttpResponse httpResponse = HttpResponse.builder()
                .message("Release Note Updated")
                .description("Release Note ID : " + 1L + " Updated")
                .build();

        List<BlockContextUpdateRequestDTO> blockContextUpdateRequestDTOList = new ArrayList<>();
        List<NoteBlockUpdateRequestDTO> noteBlockUpdateRequestDTOList = new ArrayList<>();

        BlockContextUpdateRequestDTO blockContextUpdateRequestDTO = new BlockContextUpdateRequestDTO();
        blockContextUpdateRequestDTO.setContext("test");
        blockContextUpdateRequestDTO.setTag("H1");
        blockContextUpdateRequestDTO.setIndex(1L);

        blockContextUpdateRequestDTOList.add(blockContextUpdateRequestDTO);

        NoteBlockUpdateRequestDTO noteBlockUpdateRequestDTO = new NoteBlockUpdateRequestDTO();
        noteBlockUpdateRequestDTO.setLabel("update");
        noteBlockUpdateRequestDTO.setContexts(blockContextUpdateRequestDTOList);

        noteBlockUpdateRequestDTOList.add(noteBlockUpdateRequestDTO);

        ReleaseNoteUpdateRequestDTO releaseNoteUpdateRequestDTO = new ReleaseNoteUpdateRequestDTO();
        releaseNoteUpdateRequestDTO.setBlocks(noteBlockUpdateRequestDTOList);
        releaseNoteUpdateRequestDTO.setVersion("1.0.0");
        releaseNoteUpdateRequestDTO.setReleaseDate(new Date());

        given(releaseNoteService.updateReleaseNote(any(HttpServletRequest.class),eq(1L),any(ReleaseNoteUpdateRequestDTO.class)))
                .willReturn(httpResponse);

        mvc.perform(patch("/api/project/release-note/1")
                        .requestAttr("id", 1L)
                        .requestAttr("username", "Kim")
                        .content(objectMapper.writeValueAsString(releaseNoteUpdateRequestDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(httpResponse)));
    }

    @Test
    @DisplayName("릴리즈 노트 로딩 컨트롤러 테스트")
    void loadReleaseNote() throws Exception {
        ReleaseNoteContentResponseDTO releaseNoteContentResponseDTO = new ReleaseNoteContentResponseDTO();
        releaseNoteContentResponseDTO.setReleaseDate(new Date());
        releaseNoteContentResponseDTO.setLastModified(new Date());
        releaseNoteContentResponseDTO.setBlocks(null);
        releaseNoteContentResponseDTO.setComment(null);
        releaseNoteContentResponseDTO.setCreator("Kim");
        releaseNoteContentResponseDTO.setLiked(0);
        releaseNoteContentResponseDTO.setCount(0);
        releaseNoteContentResponseDTO.setVersion("1.0.0");
        releaseNoteContentResponseDTO.setSummary("test");

        given(releaseNoteService.loadReleaseNote(any(HttpServletRequest.class),eq(1L)))
                .willReturn(releaseNoteContentResponseDTO);

        mvc.perform(get("/api/project/release-note/1")
                        .requestAttr("id", 1L)
                        .requestAttr("username", "Kim")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(releaseNoteContentResponseDTO)));
    }

    @Test
    @DisplayName("릴리즈 노트 리스트 로딩 컨트롤러 테스트")
    void loadReleaseNoteList() throws Exception {
        ArrayList<ReleaseNoteContentListResponseDTO> releaseNoteContentListResponseDTOList = new ArrayList<>();

        ReleaseNoteContentListResponseDTO releaseNoteContentListResponseDTO = new ReleaseNoteContentListResponseDTO();
        releaseNoteContentListResponseDTO.setReleaseDate(new Date());
        releaseNoteContentListResponseDTO.setLastModified(new Date());
        releaseNoteContentListResponseDTO.setVersion("1.0.0");
        releaseNoteContentListResponseDTO.setSummary("test");
        releaseNoteContentListResponseDTO.setCreator("Kim");

        releaseNoteContentListResponseDTOList.add(releaseNoteContentListResponseDTO);

        given(releaseNoteService.loadReleaseNoteList(eq(1L)))
                .willReturn(releaseNoteContentListResponseDTOList);

        mvc.perform(get("/api/project/1/release-notes")
                        .requestAttr("id", 1L)
                        .requestAttr("username", "Kim")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(releaseNoteContentListResponseDTOList)));
    }

    @Test
    @DisplayName("릴리즈 노트 버전 리스트 로딩 컨트롤러 테스트")
    void loadProjectVersionList() throws Exception {
        ArrayList<ReleaseNoteVersionListResponseDTO> releaseNoteVersionListResponseDTOList = new ArrayList<>();

        ReleaseNoteVersionListResponseDTO releaseNoteVersionListResponseDTO = new ReleaseNoteVersionListResponseDTO();

        releaseNoteVersionListResponseDTO.setProjectId(1L);
        releaseNoteVersionListResponseDTO.setReleaseNoteVersionList(null);
        releaseNoteVersionListResponseDTO.setProjectName("test");
        releaseNoteVersionListResponseDTO.setSubscribe(Boolean.TRUE);

        releaseNoteVersionListResponseDTOList.add(releaseNoteVersionListResponseDTO);

        given(releaseNoteService.loadProjectVersionList(any(HttpServletRequest.class)))
                .willReturn(releaseNoteVersionListResponseDTOList);

        mvc.perform(get("/api/project/release-note/version-list")
                        .requestAttr("id", 1L)
                        .requestAttr("username", "Kim")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(releaseNoteVersionListResponseDTOList)));
    }

    @Test
    @DisplayName("릴리즈 노트 삭제 컨트롤러 테스트")
    void deleteReleaseNote() throws Exception {
        HttpResponse httpResponse = HttpResponse.builder()
                .message("Release Note Deleted")
                .description("Release Note ID : " + 1L + " Deleted")
                .build();

        given(releaseNoteService.deleteReleaseNote(eq(1L)))
                .willReturn(httpResponse);

        mvc.perform(delete("/api/project/release-note/1")
                        .requestAttr("id", 1L)
                        .requestAttr("username", "Kim")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(httpResponse)));
    }

    @Test
    @DisplayName("최근 릴리즈 노트 로 컨트롤러 테스트")
    void loadRecentReleaseNote() throws Exception {
        ReleaseNoteContentResponseDTO releaseNoteContentResponseDTO = new ReleaseNoteContentResponseDTO();
        releaseNoteContentResponseDTO.setReleaseDate(new Date());
        releaseNoteContentResponseDTO.setLastModified(new Date());
        releaseNoteContentResponseDTO.setBlocks(null);
        releaseNoteContentResponseDTO.setComment(null);
        releaseNoteContentResponseDTO.setCreator("Kim");
        releaseNoteContentResponseDTO.setLiked(0);
        releaseNoteContentResponseDTO.setCount(0);
        releaseNoteContentResponseDTO.setVersion("1.0.0");
        releaseNoteContentResponseDTO.setSummary("test");

        given(releaseNoteService.loadRecentReleaseNote(any(HttpServletRequest.class)))
                .willReturn(releaseNoteContentResponseDTO);

        mvc.perform(get("/api/project/release-note/recent-release-note")
                        .requestAttr("id", 1L)
                        .requestAttr("username", "Kim")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(releaseNoteContentResponseDTO)));
    }
}