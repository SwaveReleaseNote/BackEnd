package com.swave.urnr.project.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swave.urnr.project.domain.Project;
import com.swave.urnr.project.exception.NotAuthorizedException;
import com.swave.urnr.project.requestdto.ProjectCreateRequestDTO;
import com.swave.urnr.project.requestdto.ProjectUpdateRequestDTO;
import com.swave.urnr.project.responsedto.ProjectContentResponseDTO;
import com.swave.urnr.project.responsedto.ProjectListResponseDTO;
import com.swave.urnr.project.responsedto.ProjectManagementContentResponseDTO;
import com.swave.urnr.project.service.ProjectService;
import com.swave.urnr.releasenote.requestdto.ReleaseNoteCreateRequestDTO;
import com.swave.urnr.releasenote.requestdto.ReleaseNoteUpdateRequestDTO;
import com.swave.urnr.user.domain.User;
import com.swave.urnr.user.domain.UserInProject;
import com.swave.urnr.user.repository.UserRepository;
import com.swave.urnr.user.responsedto.UserMemberInfoResponseDTO;
import com.swave.urnr.util.http.HttpResponse;
import com.swave.urnr.util.type.UserRole;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Persistable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ProjectController.class)
class ProjectControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mvc;

    @MockBean
    private ProjectService projectService;


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
    void createProject() throws Exception {

        HttpResponse httpResponse = HttpResponse.builder()
                .message("Project Created")
                .description("Project ID : " + 1L + " Created")
                .build();

        userTest1 = User.builder()
                .name("kang")
                .email("admin@naver.com")
                .provider("server")
                .password("1234")
                .build();
        userRepository.save(userTest1);

        //String token = userService.getTokenByLogin(userLoginServerRequestDTO);

        userTest2 = User.builder()
                .name("kim")
                .email("korea2@naver.com")
                .provider("server")
                .password("1232")
                .build();


        userTest3 = User.builder()
                .name("jin")
                .email("korea3@naver.com")
                .provider("server")
                .password("1233")
                .build();

        userTest4 = User.builder()
                .name("john")
                .email("korea4@naver.com")
                .provider("server")
                .password("1234")
                .build();

        userTest5 = User.builder()
                .name("user5")
                .email("korea5@naver.com")
                .provider("server")
                .password("1235")
                .build();


        userRepository.save(userTest2);

        userRepository.save(userTest3);

        userRepository.save(userTest4);

        userRepository.save(userTest5);


        User user = User.builder()
                .email("test@gmail.com")
                .name("joe")
                .password("1q2w3e4r5t")
                .provider("local")
                .build();
        userRepository.saveAndFlush(user);

        ProjectCreateRequestDTO projectCreateRequestDTO = ProjectCreateRequestDTO.builder()
                .projectName("SwaveForm")
                .description("굳잡")
                .build();

        List<Long> users = new ArrayList<>(){
            {
                add(userTest1.getId());
                add(userTest2.getId());
                add(userTest3.getId());
            }
        };
        //List<Long> users = new ArrayList<>();
        projectCreateRequestDTO.setUsers(users);



        given(projectService.createProject(any(HttpServletRequest.class),any(ProjectCreateRequestDTO.class)))
                .willReturn(httpResponse);

        mvc.perform(post("/api/project")
                        .requestAttr("id", 1L)
                        .requestAttr("username", "joe")
                            //아무나 해도 다되는데?
                         .content(objectMapper.writeValueAsString(projectCreateRequestDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(httpResponse)));


    }

    @Test
    void loadProjectList() throws Exception {
        ArrayList<ProjectListResponseDTO> loadProjectList = new ArrayList<>();
        ProjectListResponseDTO projectListResponseDTO = new ProjectListResponseDTO();
        projectListResponseDTO.setName("SwaveForm");
        projectListResponseDTO.setId(1L);
        projectListResponseDTO.setDescription("굳잡");
        projectListResponseDTO.setRole(UserRole.Manager);
        projectListResponseDTO.setCount(4);
        projectListResponseDTO.setVersion("1.0.0");
        projectListResponseDTO.setCreateDate(new Date());

        loadProjectList.add(projectListResponseDTO);


        given(projectService.loadProjectList(any(HttpServletRequest.class)))
                .willReturn(loadProjectList);

        mvc.perform(get("/api/projects")
                        .requestAttr("id", 1L)
                        .requestAttr("username", "Kim")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(loadProjectList)));
    }

    @Test
    void loadProject() throws Exception {
        ProjectContentResponseDTO projectContentResponseDTO = new ProjectContentResponseDTO();
        projectContentResponseDTO.setCreateDate(new Date());
        projectContentResponseDTO.setId(1L);
        projectContentResponseDTO.setDescription("굳잡");
        projectContentResponseDTO.setName("SwaveForm");

        given(projectService.loadProject(any(HttpServletRequest.class),eq(1L)))
                .willReturn(projectContentResponseDTO);

        mvc.perform(get("/api/project/1")
                        .requestAttr("id", 1L)
                        .requestAttr("username", "Kang")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(projectContentResponseDTO)));


    }

    @Test
    void updateProject() throws Exception {
        HttpResponse httpResponse = HttpResponse.builder()
                .message("Project Updated")
                .description("Project ID : " + 1L + " Updated")
                .build();


        userTest1 = User.builder()
                .name("kang")
                .email("admin@naver.com")
                .provider("server")
                .password("1234")
                .build();
        userRepository.save(userTest1);

        //String token = userService.getTokenByLogin(userLoginServerRequestDTO);

        userTest2 = User.builder()
                .name("kim")
                .email("korea2@naver.com")
                .provider("server")
                .password("1232")
                .build();


        userTest3 = User.builder()
                .name("jin")
                .email("korea3@naver.com")
                .provider("server")
                .password("1233")
                .build();

        userTest4 = User.builder()
                .name("john")
                .email("korea4@naver.com")
                .provider("server")
                .password("1234")
                .build();

        userTest5 = User.builder()
                .name("user5")
                .email("korea5@naver.com")
                .provider("server")
                .password("1235")
                .build();


        userRepository.save(userTest2);

        userRepository.save(userTest3);

        userRepository.save(userTest4);

        userRepository.save(userTest5);

        ProjectUpdateRequestDTO projectUpdateRequestDTO = new ProjectUpdateRequestDTO();
        projectUpdateRequestDTO.setName("안녕");
        projectUpdateRequestDTO.setDescription("잘가");
        List<Long> updateUsers = new ArrayList<>(){
            {
                add(userTest4.getId());
                add(userTest5.getId());
            }
        };
        projectUpdateRequestDTO.setUpdateUsers(updateUsers);
        List<Long> deleteUsers = new ArrayList<>(){
            {
                add(userTest2.getId());
                add(userTest3.getId());
            }
        };
        projectUpdateRequestDTO.setDeleteUsers(deleteUsers);
        given(projectService.updateProject(any(HttpServletRequest.class),eq(1L),any(ProjectUpdateRequestDTO.class)))
                .willReturn(projectUpdateRequestDTO);

        mvc.perform(put("/api/project/1")
                        .requestAttr("id", 1L)
                        .requestAttr("username", "Kang")
                        .content(objectMapper.writeValueAsString(projectUpdateRequestDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(projectUpdateRequestDTO)));
    }

    @Test
    void deleteProject() throws Exception {
        HttpResponse httpResponse = HttpResponse.builder()
                .message("Project Deleted")
                .description("Project ID : " + 1L + " Deleted")
                .build();

        given(projectService.deleteProject(any(HttpServletRequest.class),eq(1L)))
                .willReturn(httpResponse);

        mvc.perform(delete("/api/project/1")
                        .requestAttr("id", 1L)
                        .requestAttr("username", "Kang")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(httpResponse)));
    }

    @Test
    void loadManagementProject() throws Exception {


        userTest1 = User.builder()
                .name("kang")
                .email("admin@naver.com")
                .provider("server")
                .password("1234")
                .build();
        userRepository.save(userTest1);

        //String token = userService.getTokenByLogin(userLoginServerRequestDTO);

        userTest2 = User.builder()
                .name("kim")
                .email("korea2@naver.com")
                .provider("server")
                .password("1232")
                .build();


        userTest3 = User.builder()
                .name("jin")
                .email("korea3@naver.com")
                .provider("server")
                .password("1233")
                .build();

        userTest4 = User.builder()
                .name("john")
                .email("korea4@naver.com")
                .provider("server")
                .password("1234")
                .build();

        userTest5 = User.builder()
                .name("user5")
                .email("korea5@naver.com")
                .provider("server")
                .password("1235")
                .build();


        userRepository.save(userTest2);

        userRepository.save(userTest3);

        userRepository.save(userTest4);

        userRepository.save(userTest5);
        ProjectManagementContentResponseDTO projectManagementContentResponseDTO = new ProjectManagementContentResponseDTO();
        projectManagementContentResponseDTO.setId(1L);
        projectManagementContentResponseDTO.setManagerId(1L);
        projectManagementContentResponseDTO.setManagerName("Kang");
        projectManagementContentResponseDTO.setCreateDate(new Date());
        projectManagementContentResponseDTO.setName("안녕");
        projectManagementContentResponseDTO.setDescription("잘가");
        projectManagementContentResponseDTO.setManagerDepartment(null);
        List<UserMemberInfoResponseDTO> teamMembers = new ArrayList<>(){
            {
                add(new UserMemberInfoResponseDTO(userTest4.getId(),userTest4.getUsername(),userTest4.getDepartment()));
                add(new UserMemberInfoResponseDTO(userTest5.getId(),userTest5.getUsername(),userTest5.getDepartment()));
            }
        };
        projectManagementContentResponseDTO.setTeamMembers(teamMembers);

        given(projectService.loadManagementProject(any(HttpServletRequest.class),eq(1L)))
                .willReturn(projectManagementContentResponseDTO);

        mvc.perform(get("/api/project/1/manage")
                        .requestAttr("id", 1L)
                        .requestAttr("username", "Kang")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(projectManagementContentResponseDTO)));



    }
}