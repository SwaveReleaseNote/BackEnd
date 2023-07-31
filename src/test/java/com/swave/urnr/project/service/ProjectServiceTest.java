package com.swave.urnr.project.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.swave.urnr.project.domain.Project;
import com.swave.urnr.project.repository.ProjectRepository;
import com.swave.urnr.project.requestdto.ProjectCreateRequestDTO;
import com.swave.urnr.project.requestdto.ProjectUpdateRequestDTO;
import com.swave.urnr.project.responsedto.ProjectContentResponseDTO;
import com.swave.urnr.project.responsedto.ProjectListResponseDTO;
import com.swave.urnr.releasenote.domain.ReleaseNote;
import com.swave.urnr.user.domain.User;
import com.swave.urnr.user.domain.UserInProject;
import com.swave.urnr.user.exception.UserNotFoundException;
import com.swave.urnr.user.repository.UserInProjectRepository;
import com.swave.urnr.user.repository.UserRepository;
import com.swave.urnr.user.requestdto.UserLoginServerRequestDTO;
import com.swave.urnr.user.service.UserService;
import com.swave.urnr.util.http.HttpResponse;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProjectServiceTest {

    @Autowired
    ProjectService projectService;

    @Autowired
    UserService userService;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    UserInProjectRepository userInProjectRepository;

    @Autowired
    UserRepository userRepository;

    MockHttpServletRequest request;

    @BeforeEach
    @DisplayName("사용자 등록")
    void setUp() throws UserNotFoundException {


        User user = User.builder()
                .name("kang")
                .email("admin@naver.com")
                .provider("server")
                .password("1234")
                .build();
        userRepository.save(user);

        UserLoginServerRequestDTO userLoginServerRequestDTO =  new UserLoginServerRequestDTO(user.getEmail(),user.getPassword());

        //String token = userService.getTokenByLogin(userLoginServerRequestDTO);

        User user2 = User.builder()
                .name("kim")
                .email("korea2@naver.com")
                .provider("server")
                .password("1232")
                .build();


        User user3 = User.builder()
                .name("jin")
                .email("korea3@naver.com")
                .provider("server")
                .password("1233")
                .build();
        User user4 = User.builder()
                .name("john")
                .email("korea4@naver.com")
                .provider("server")
                .password("1234")
                .build();
        User user5 = User.builder()
                .name("user5")
                .email("korea5@naver.com")
                .provider("server")
                .password("1235")
                .build();

        userRepository.save(user2);

        userRepository.save(user3);

        userRepository.save(user4);

        userRepository.save(user5);

        MockHttpSession httpSession = new MockHttpSession();
        request = new MockHttpServletRequest();
        request.setSession(httpSession);
        request.setAttribute("id", 1L);
        request.setAttribute("username", "kang");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

    }

    @Test
    @Transactional
    void createProject() {
        //테스트 코드는 무언가를 받을 필요가 없나?
        System.out.println("시작");
        ProjectCreateRequestDTO projectCreateRequestDTO = ProjectCreateRequestDTO.builder()
                .projectName("SwaveForm")
                .description("굳잡")
                .build();

        List<Long> users = new ArrayList<>(){
            {
                add(2L);
                add(3L);
                add(4L);
            }
        };
        projectCreateRequestDTO.setUsers(users);

        //projectCreateRequestDTO.setUserId((Long)request.getAttribute("id"));


        HttpResponse httpResponse = projectService.createProject(request,projectCreateRequestDTO);

        Pattern pattern = Pattern.compile("\\d+"); // Matches one or more digits
        Matcher matcher = pattern.matcher(httpResponse.getDescription());
        if (matcher.find()) {
            String numberString = matcher.group(); // Extract the matched digits as a string
            long number = Long.parseLong(numberString); // Convert the string to an integer

            Project project = projectRepository.findById(number).get();
            //ReleaseNote releaseNote = releaseNoteRepository.findById(Long.parseLong(httpResponse.getDescription().substring(18,21).replace(" ","").replace("C", ""))).orElse(null);

            assertEquals(httpResponse.getDescription(),"Project Id "+number+" created");
            assertEquals(project.getName(),"SwaveForm");
            assertEquals(project.getDescription(),"굳잡");
            //assertEquals(number,"8");
        }


    }

    @Test
    void loadProjectList() {


        System.out.println("시작");
        ProjectCreateRequestDTO projectCreateRequestDTO = ProjectCreateRequestDTO.builder()
                .projectName("SwaveForm")
                .description("굳잡")
                .build();

        List<Long> users = new ArrayList<>(){
            {
                add(2L);
                add(3L);
                add(4L);
            }
        };
        projectCreateRequestDTO.setUsers(users);

        HttpResponse httpResponse = projectService.createProject(request,projectCreateRequestDTO);
        
        List<ProjectListResponseDTO> projectListResponseDTOList = projectService.loadProjectList(request);




        Pattern pattern = Pattern.compile("\\d+"); // Matches one or more digits
        Matcher matcher = pattern.matcher(httpResponse.getDescription());
        if (matcher.find()) {
            String numberString = matcher.group(); // Extract the matched digits as a string
            long number = Long.parseLong(numberString); // Convert the string to an integer

            Project project = projectRepository.findById(number).get();
            //ReleaseNote releaseNote = releaseNoteRepository.findById(Long.parseLong(httpResponse.getDescription().substring(18,21).replace(" ","").replace("C", ""))).orElse(null);

            assertEquals(httpResponse.getDescription(),"Project Id "+number+" created");
            assertEquals(project.getName(),"SwaveForm");
            assertEquals(project.getDescription(),"굳잡");
            //assertEquals(number,"8");
        }

        assertEquals(projectListResponseDTOList.get(0).getName(),"가나");
        assertEquals(projectListResponseDTOList.get(0).getDescription(),"안정");
        assertEquals(projectListResponseDTOList.get(0).getId(),2L);
    }

    @Test
    void loadProject() {
        Long projectId = 3L;
        ProjectContentResponseDTO projectContentResponseDTO =  projectService.loadProject(projectId);

        assertEquals(projectContentResponseDTO.getName(),"니거무라");
        assertEquals(projectContentResponseDTO.getDescription(),"이거도");
        assertEquals(projectContentResponseDTO.getId(),3L);
    }

    @Test
    void updateProject() {
        Long projectId = 3L;

        List<Long> deleteUsers = new ArrayList<>(){
            {
                add(4L);
            }
        };
        List<Long> updateUsers = new ArrayList<>(){
            {

                add(66L);
            }
        };

        ProjectUpdateRequestDTO projectUpdateRequestDTO = ProjectUpdateRequestDTO.builder()
                .name("집가고싶다")
                .description("안녕하세요")
                .deleteUsers(deleteUsers)
                .updateUsers(updateUsers)
                .build();

        ProjectUpdateRequestDTO projectUpdateResponseDTO = projectService.updateProject(projectId,projectUpdateRequestDTO);


        assertEquals(projectUpdateResponseDTO.getName(),"집가고싶다");
        assertEquals(projectUpdateResponseDTO.getDescription(),"안녕하세요");
        assertEquals(projectUpdateResponseDTO.getDeleteUsers(),projectUpdateRequestDTO.getDeleteUsers());
        assertEquals(projectUpdateResponseDTO.getUpdateUsers(),projectUpdateRequestDTO.getUpdateUsers());

        
    }

    @Test
    void deleteProject() {

        Long projectId = 3L;
        HttpResponse httpResponse = projectService.deleteProject(projectId);
        assertEquals(httpResponse.getDescription(),"Project Id 3 deleted");

    }
}