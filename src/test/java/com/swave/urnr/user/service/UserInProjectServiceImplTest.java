package com.swave.urnr.user.service;

import com.swave.urnr.project.domain.Project;
import com.swave.urnr.project.repository.ProjectRepository;
import com.swave.urnr.project.requestdto.ProjectCreateRequestDTO;
import com.swave.urnr.project.responsedto.ProjectListResponseDTO;
import com.swave.urnr.project.service.ProjectService;
import com.swave.urnr.user.domain.User;
import com.swave.urnr.user.domain.UserInProject;
import com.swave.urnr.user.exception.UserNotFoundException;
import com.swave.urnr.user.repository.UserInProjectRepository;
import com.swave.urnr.user.repository.UserRepository;
import com.swave.urnr.util.http.HttpResponse;
import com.swave.urnr.util.type.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

class UserInProjectServiceImplTest {

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

    @Autowired
    UserInProjectService userInProjectService;

    MockHttpServletRequest request;

    User userTest1;
    User userTest2;
    User userTest3;
    User userTest4;
    User userTest5;



    @BeforeEach
    @Transactional
    @DisplayName("사용자 등록")
    void setUp() throws UserNotFoundException {


        MockHttpSession httpSession = new MockHttpSession();
        request = new MockHttpServletRequest();

    }

    @Test
    @Transactional
    @DisplayName("구독 탈퇴 테스트")
    void dropProject() {

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


        System.out.println("시작");
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


        projectCreateRequestDTO.setUsers(users);


        request.setAttribute("id",user.getId());

        user.setUserInProjectList(new ArrayList<>());

        HttpResponse httpResponse = projectService.createProject(request,projectCreateRequestDTO);


        Pattern pattern = Pattern.compile("\\d+"); // Matches one or more digits
        Matcher matcher = pattern.matcher(httpResponse.getDescription());

        long number=0;

        if (matcher.find()) {
            String numberString = matcher.group(); // Extract the matched digits as a string
            number = Long.parseLong(numberString); // Convert the string to an integer

            Project project = projectRepository.findById(number).get();
            //ReleaseNote releaseNote = releaseNoteRepository.findById(Long.parseLong(httpResponse.getDescription().substring(18,21).replace(" ","").replace("C", ""))).orElse(null);

            assertEquals(httpResponse.getDescription(),"Project Id "+number+" created");
            assertEquals(project.getName(),"SwaveForm");
            assertEquals(project.getDescription(),"굳잡");
            //assertEquals(number,"8");
        }
        Long projectId = number;


        request.setAttribute("id",userTest4.getId());
        userInProjectService.subscribeProject(request,projectId);

        userInProjectService.dropProject(request, projectId);
        UserInProject userInProject = userInProjectRepository.findByUser_IdAndProject_Id((Long)request.getAttribute("id"),projectId);

        assertEquals(userInProject,null);
    }

    @Test
    @Transactional
    @DisplayName("구독 테스트")
    void subscribeProject() {

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


        System.out.println("시작");
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


        projectCreateRequestDTO.setUsers(users);


        request.setAttribute("id",user.getId());


        user.setUserInProjectList(new ArrayList<>());

        HttpResponse httpResponse = projectService.createProject(request,projectCreateRequestDTO);


        Pattern pattern = Pattern.compile("\\d+"); // Matches one or more digits
        Matcher matcher = pattern.matcher(httpResponse.getDescription());

        long number=0;

        if (matcher.find()) {
            String numberString = matcher.group(); // Extract the matched digits as a string
            number = Long.parseLong(numberString); // Convert the string to an integer

            Project project = projectRepository.findById(number).get();
            //ReleaseNote releaseNote = releaseNoteRepository.findById(Long.parseLong(httpResponse.getDescription().substring(18,21).replace(" ","").replace("C", ""))).orElse(null);

            assertEquals(httpResponse.getDescription(),"Project Id "+number+" created");
            assertEquals(project.getName(),"SwaveForm");
            assertEquals(project.getDescription(),"굳잡");
            //assertEquals(number,"8");
        }
        Long projectId = number;


        request.setAttribute("id",userTest4.getId());
        HttpResponse httpResponse1 = userInProjectService.subscribeProject(request,projectId);
        UserInProject userInProject = userInProjectRepository.findByUser_IdAndProject_Id((Long)request.getAttribute("id"),projectId);


        //assertEquals(httpResponse1,"SwaveForm");
        assertEquals(userInProject.getRole(), UserRole.Subscriber);
        assertEquals(userInProject.getProject().getName(), "SwaveForm");
        assertEquals(userInProject.getUser().getId(), userTest4.getId());



    }

    @Test
    @Transactional
    @DisplayName("역할 검색 테스트")
    void getRole() {

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


        request.setAttribute("id",user.getId());


        user.setUserInProjectList(new ArrayList<>());


        HttpResponse httpResponse = projectService.createProject(request,projectCreateRequestDTO);

        Pattern pattern = Pattern.compile("\\d+"); // Matches one or more digits
        Matcher matcher = pattern.matcher(httpResponse.getDescription());
        long number = 0;
        if (matcher.find()) {
            String numberString = matcher.group(); // Extract the matched digits as a string
            number = Long.parseLong(numberString); // Convert the string to an integer

            Project project = projectRepository.findById(number).get();
            //ReleaseNote releaseNote = releaseNoteRepository.findById(Long.parseLong(httpResponse.getDescription().substring(18,21).replace(" ","").replace("C", ""))).orElse(null);

            assertEquals(httpResponse.getDescription(),"Project Id "+number+" created");
            assertEquals(project.getName(),"SwaveForm");
            assertEquals(project.getDescription(),"굳잡");
            //assertEquals(number,"8");
        }

        UserRole role = userInProjectService.getRole(request,number);


        assertEquals(role,UserRole.Manager);
    }
}