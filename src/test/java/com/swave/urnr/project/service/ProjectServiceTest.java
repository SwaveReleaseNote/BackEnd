package com.swave.urnr.project.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.swave.urnr.project.domain.Project;
import com.swave.urnr.project.exception.NotAuthorizedException;
import com.swave.urnr.project.repository.ProjectRepository;
import com.swave.urnr.project.requestdto.ProjectCreateRequestDTO;
import com.swave.urnr.project.requestdto.ProjectKeywordRequestContentDTO;
import com.swave.urnr.project.requestdto.ProjectUpdateRequestDTO;
import com.swave.urnr.project.responsedto.ProjectContentResponseDTO;
import com.swave.urnr.project.responsedto.ProjectListResponseDTO;
import com.swave.urnr.project.responsedto.ProjectSearchResultListResponseDTO;
import com.swave.urnr.releasenote.domain.ReleaseNote;
import com.swave.urnr.user.domain.User;
import com.swave.urnr.user.domain.UserInProject;
import com.swave.urnr.user.exception.UserNotFoundException;
import com.swave.urnr.user.repository.UserInProjectRepository;
import com.swave.urnr.user.repository.UserRepository;
import com.swave.urnr.user.requestdto.UserLoginServerRequestDTO;
import com.swave.urnr.user.service.UserService;
import com.swave.urnr.util.http.HttpResponse;
import com.swave.urnr.util.type.UserRole;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
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
    @DisplayName("프로젝트 생성 테스트")
    void createProject() {


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
        if (matcher.find()) {
            String numberString = matcher.group(); // Extract the matched digits as a string
            long number = Long.parseLong(numberString); // Convert the string to an integer

            Project project = projectRepository.findById(number).orElse(null);
            //ReleaseNote releaseNote = releaseNoteRepository.findById(Long.parseLong(httpResponse.getDescription().substring(18,21).replace(" ","").replace("C", ""))).orElse(null);
            System.out.println(project.getUserInProjectList().get(0).getUser().getUsername());
            System.out.println(project.getUserInProjectList().get(0).getRole());
            System.out.println(project.getUserInProjectList().get(1).getUser().getUsername());
            System.out.println(project.getUserInProjectList().get(1).getRole());
            System.out.println(project.getUserInProjectList().get(2).getUser().getUsername());
            System.out.println(project.getUserInProjectList().get(2).getRole());
            System.out.println(project.getUserInProjectList().get(3).getUser().getUsername());
            System.out.println(project.getUserInProjectList().get(3).getRole());
            List<UserInProject> userInProjectList = project.getUserInProjectList();

            assertEquals(httpResponse.getDescription(),"Project Id "+number+" created");
            assertEquals(project.getName(),"SwaveForm");
            assertEquals(project.getDescription(),"굳잡");
            assertEquals(project.getUserInProjectList().get(0).getUser().getId(),user.getId());
            assertEquals(project.getUserInProjectList().get(1).getUser().getId(),userTest1.getId());
            assertEquals(project.getUserInProjectList().get(2).getUser().getId(),userTest2.getId());
            assertEquals(project.getUserInProjectList().get(3).getUser().getId(),userTest3.getId());
            assertEquals(project.getUserInProjectList().get(2).getRole(), UserRole.Developer);
            //assertEquals(number,"8");
        }


    }

    @Test
    @Transactional
    @DisplayName("프로젝트 리스트 로딩")
    void loadProjectList() {


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


        assertEquals(projectListResponseDTOList.get(0).getName(),"SwaveForm");
        assertEquals(projectListResponseDTOList.get(0).getDescription(),"굳잡");

    }

    @Test
    @Transactional
    @DisplayName("프로젝트 로딩 테스트")
    void loadProject() throws NotAuthorizedException {


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

        ProjectContentResponseDTO projectContentResponseDTO =  projectService.loadProject(request,number);

        assertEquals(projectContentResponseDTO.getName(),"SwaveForm");
        assertEquals(projectContentResponseDTO.getDescription(),"굳잡");
        assertEquals(projectContentResponseDTO.getId(),number);
    }

    @Test
    @DisplayName("프로젝트 업데이트 테스트")
    void updateProject() throws NotAuthorizedException {


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

        List<Long> deleteUsers = new ArrayList<>(){
            {
                add(userTest3.getId());
            }
        };
        List<Long> updateUsers = new ArrayList<>(){
            {
                add(userTest5.getId());
            }
        };

        ProjectUpdateRequestDTO projectUpdateRequestDTO = ProjectUpdateRequestDTO.builder()
                .name("집가고싶다")
                .description("안녕하세요")
                .deleteUsers(deleteUsers)
                .updateUsers(updateUsers)
                .build();

        ProjectUpdateRequestDTO projectUpdateResponseDTO = projectService.updateProject(request,projectId,projectUpdateRequestDTO);


        assertEquals(projectUpdateResponseDTO.getName(),"집가고싶다");
        assertEquals(projectUpdateResponseDTO.getDescription(),"안녕하세요");
        assertEquals(projectUpdateResponseDTO.getDeleteUsers(),projectUpdateRequestDTO.getDeleteUsers());
        assertEquals(projectUpdateResponseDTO.getUpdateUsers(),projectUpdateRequestDTO.getUpdateUsers());


    }

    @Test
    @Transactional
    @DisplayName("프로젝트 삭제 테스트")
    void deleteProject() throws NotAuthorizedException {

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

        List<ProjectListResponseDTO> projectListResponseDTOList = projectService.loadProjectList(request);


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

        projectService.deleteProject(request, projectId);
        Project project = projectRepository.findById(projectId).orElse(null);

        assertEquals(project,null);

    }

    @Test
    @Transactional
    @DisplayName("역할 가져오기 테스트")
    void getRole(){
        //유저 만들고
        //프로젝트 생성

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

        UserRole role = projectService.getRole(request,number);


        assertEquals(role,UserRole.Manager);


    }

    @Test
    @Transactional
    @DisplayName("프로젝트 검색 테스트")
    void searchProject() throws UnsupportedEncodingException {
        //유저 만들고
        //프로젝트 생성

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

        ProjectKeywordRequestContentDTO projectKeywordRequestContentDTO = new ProjectKeywordRequestContentDTO();
        projectKeywordRequestContentDTO.setKeyword("Swave");

        ProjectSearchResultListResponseDTO projectSearchResultListResponseDTO = projectService.searchProject(projectKeywordRequestContentDTO);

        assertEquals(projectSearchResultListResponseDTO.getTitleSearch().get(0).getName(),"SwaveForm");
    }

}