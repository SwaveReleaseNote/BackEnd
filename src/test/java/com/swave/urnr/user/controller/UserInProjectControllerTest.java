package com.swave.urnr.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swave.urnr.project.controller.ProjectController;
import com.swave.urnr.project.repository.ProjectRepository;
import com.swave.urnr.project.requestdto.ProjectCreateRequestDTO;
import com.swave.urnr.project.service.ProjectService;
import com.swave.urnr.releasenote.repository.NoteBlockRepository;
import com.swave.urnr.releasenote.repository.ReleaseNoteRepository;
import com.swave.urnr.releasenote.repository.SeenCheckRepository;
import com.swave.urnr.releasenote.service.ReleaseNoteService;
import com.swave.urnr.user.domain.User;
import com.swave.urnr.user.repository.UserInProjectRepository;
import com.swave.urnr.user.repository.UserRepository;
import com.swave.urnr.user.service.UserInProjectService;
import com.swave.urnr.util.http.HttpResponse;
import com.swave.urnr.util.type.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = UserInProjectController.class)
class UserInProjectControllerTest {


    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mvc;

    @MockBean
    private UserInProjectService userInProjectService;


    @MockBean
    private UserRepository userRepository;


    User userTest1;
    User userTest2;
    User userTest3;
    User userTest4;
    User userTest5;


    @BeforeEach
    void setUp(WebApplicationContext applicationContext) {
        mvc = MockMvcBuilders.webAppContextSetup(applicationContext)
                .addFilter(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true))
                .build();

    }


    @Test
    void dropProject() throws Exception {

        HttpResponse httpResponse = HttpResponse.builder()
                .message("User Delete")
                .description(1L + "Completed")
                .build();

        userTest1 = User.builder()
                .name("kang")
                .email("admin@naver.com")
                .provider("server")
                .password("1234")
                .build();
        userRepository.save(userTest1);

        given(userInProjectService.subscribeProject(any(HttpServletRequest.class),eq(userTest1.getId())))
                .willReturn(httpResponse);

        mvc.perform(delete("/api/project/1/drop")
                        .requestAttr("id", 1L)
                        .requestAttr("username", "joe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void subscribeProject() throws Exception {

        HttpResponse httpResponse = HttpResponse.builder()
                .message("User Subscribe Created")
                .description(1L + "Completed")
                .build();


        given(userInProjectService.subscribeProject(any(HttpServletRequest.class),eq(1L)))
                .willReturn(httpResponse);

        mvc.perform(post("/api/project/1/subscribe")
                        .requestAttr("id", 1L)
                        .requestAttr("username", "joe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(httpResponse)));
    }

    @Test
    void getRole() throws Exception {
        UserRole role = UserRole.Manager;

        given(userInProjectService.getRole(any(HttpServletRequest.class),eq(1L)))
                .willReturn(role);

        MvcResult result = mvc.perform(get("/api/project/1/role")
                        .requestAttr("id", 1L)
                        .requestAttr("username", "joe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();

        //String roles = "Manager";
        assertEquals("\"" + role + "\"", responseContent);

}
}