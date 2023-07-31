package com.swave.urnr.releasenote.service;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.swave.urnr.project.domain.Project;
import com.swave.urnr.project.repository.ProjectRepository;
import com.swave.urnr.releasenote.domain.*;
import com.swave.urnr.releasenote.repository.BlockContextRepository;
import com.swave.urnr.releasenote.repository.NoteBlockRepository;
import com.swave.urnr.releasenote.repository.ReleaseNoteRepository;
import com.swave.urnr.releasenote.repository.SeenCheckRepository;
import com.swave.urnr.releasenote.requestdto.*;
import com.swave.urnr.releasenote.responsedto.*;
import com.swave.urnr.user.domain.User;
import com.swave.urnr.user.domain.UserInProject;
import com.swave.urnr.user.repository.UserInProjectRepository;
import com.swave.urnr.user.repository.UserRepository;
import com.swave.urnr.util.http.HttpResponse;
import com.swave.urnr.util.querydsl.QueryDslConfig;
import com.swave.urnr.util.type.UserRole;
import io.swagger.annotations.ApiModelProperty;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import(QueryDslConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ReleaseNoteServiceImplTest {

    @Autowired
    private ReleaseNoteService releaseNoteService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserInProjectRepository userInProjectRepository;
    @Autowired
    private NoteBlockRepository noteBlockRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ReleaseNoteRepository releaseNoteRepository;
    @Autowired
    private SeenCheckRepository seenCheckRepository;

    private MockHttpServletRequest request;

    @BeforeEach
    void setUp() {
        MockHttpSession httpSession = new MockHttpSession();
        request = new MockHttpServletRequest();
        request.setSession(httpSession);
        request.setAttribute("id", 1L);
        request.setAttribute("username", "Kim");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @Test
    @Transactional
    @DisplayName("릴리즈 노트 생성 테스트")
    void createReleaseNote() {
        User user = User.builder()
                .email("test@gmail.com")
                .name("Kim")
                .password("1q2w3e4r5t")
                .provider("local")
                .build();

        userRepository.saveAndFlush(user);

        request.setAttribute("id", user.getId());

        Project project = Project.builder()
                .createDate(new Date())
                .description("test project")
                .name("name")
                .build();

        projectRepository.saveAndFlush(project);

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

        Date testDate = new Date();
        ReleaseNoteCreateRequestDTO releaseNoteCreateRequestDTO = new ReleaseNoteCreateRequestDTO();
        releaseNoteCreateRequestDTO.setReleaseDate(testDate);
        releaseNoteCreateRequestDTO.setVersion("1.9.9");
        releaseNoteCreateRequestDTO.setBlocks(noteBlockCreateRequestDTOList);

        HttpResponse httpResponse = releaseNoteService.createReleaseNote(request, project.getId(), releaseNoteCreateRequestDTO);
        ReleaseNote releaseNote = releaseNoteRepository.findById(Long.parseLong(httpResponse.getDescription().substring(18,21).replace(" ","").replace("C", ""))).orElse(null);

        Assertions.assertAll(
                () -> assertNotEquals(null, releaseNote, () -> "릴리즈 노트가 제대로 생성 되지 않았습니다."),
                () -> assertEquals("1.9.9", releaseNote.getVersion(),() -> "릴리즈 노트 버전이 제대로 생성 되지 않았습니다."),
                () -> assertEquals("new", releaseNote.getNoteBlockList().get(0).getLabel(),() -> "릴리즈 노트 내용이 제대로 생성 되지 않았습니다."),
                () -> assertEquals(testDate, releaseNote.getReleaseDate(), () -> "릴리즈 노트 날짜가 제대로 생성 되지 않았습니다.")
        );
    }

    @Test
    @Transactional
    @DisplayName("릴리즈 노트 리스트 로딩 테스트")
    void loadReleaseNoteList() {
        User user = User.builder()
                .email("test@gmail.com")
                .name("Kim")
                .password("1q2w3e4r5t")
                .provider("local")
                .build();

        userRepository.saveAndFlush(user);

        Project project = Project.builder()
                .createDate(new Date())
                .description("test project")
                .name("name")
                .build();

        projectRepository.saveAndFlush(project);

        Date testDate = new Date();
        NoteBlock noteBlock = NoteBlock.builder()
                .label("new")
                .build();

        noteBlockRepository.save(noteBlock);

        ReleaseNote releaseNote = ReleaseNote.builder()
                .version("1.9.9")
                .lastModifiedDate(testDate)
                .releaseDate(testDate)
                .count(0)
                .isUpdated(false)
                .summary("test")
                .project(project)
                .noteBlockList(new ArrayList<>(Collections.singletonList(noteBlock)))
                .user(user)
                .commentList(null)
                .build();

        releaseNoteRepository.save(releaseNote);
        noteBlock.setReleaseNote(releaseNote);

        ArrayList<ReleaseNoteContentListResponseDTO> releaseNotes = releaseNoteService.loadReleaseNoteList(project.getId());

        Assertions.assertAll(
                () -> assertNotEquals(null, releaseNotes, () -> "릴리즈 노트가 제대로 로딩 되지 않았습니다."),
                () -> assertEquals("1.9.9", releaseNotes.get(0).getVersion(),() -> "릴리즈 노트 버전이 제대로 로딩 되지 않았습니다."),
                () -> assertEquals(testDate, releaseNotes.get(0).getReleaseDate(), () -> "릴리즈 노트 날짜가 제대로 로딩 되지 않았습니다."),
                () -> assertEquals("test", releaseNotes.get(0).getSummary(), () -> "릴리즈 노트 요약이 제대로 로딩 되지 않았습니다."),
                () -> assertEquals("Kim", releaseNotes.get(0).getCreator(), () -> "랄라즈 노트 작성자가 제대로 로딩 되지 않았습니다.")
        );
    }

    @Test
    @Transactional
    @DisplayName("릴리즈 노트 로딩 테스트")
    void loadReleaseNote() {
        User user = User.builder()
                .email("test@gmail.com")
                .name("Kim")
                .password("1q2w3e4r5t")
                .provider("local")
                .build();

        userRepository.saveAndFlush(user);

        request.setAttribute("id", user.getId());

        Project project = Project.builder()
                .createDate(new Date())
                .description("test project")
                .name("name")
                .build();

        projectRepository.saveAndFlush(project);

        UserInProject userInProject = UserInProject.builder()
                .user(user)
                .project(project)
                .role(UserRole.Developer)
                .build();

        userInProjectRepository.saveAndFlush(userInProject);

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

        Date testDate = new Date();
        ReleaseNoteCreateRequestDTO releaseNoteCreateRequestDTO = new ReleaseNoteCreateRequestDTO();
        releaseNoteCreateRequestDTO.setReleaseDate(testDate);
        releaseNoteCreateRequestDTO.setVersion("1.9.9");
        releaseNoteCreateRequestDTO.setBlocks(noteBlockCreateRequestDTOList);

        HttpResponse httpResponse = releaseNoteService.createReleaseNote(request, project.getId(), releaseNoteCreateRequestDTO);
        ReleaseNoteContentResponseDTO releaseNoteContentResponseDTO = releaseNoteService.loadReleaseNote(request, Long.parseLong(httpResponse.getDescription().substring(18,21).replace(" ","").replace("C", "")));

        Assertions.assertAll(
                () -> assertEquals("new", releaseNoteContentResponseDTO.getBlocks().get(0).getLabel(), () -> "릴리즈 노트가 제대로 로딩 되지 않았습니다."),
                () -> assertEquals("1.9.9", releaseNoteContentResponseDTO.getVersion(),() -> "릴리즈 노트 버전이 제대로 로딩 되지 않았습니다."),
                () -> assertEquals("Kim", releaseNoteContentResponseDTO.getCreator(),() -> "릴리즈 노트 작성자가 제대로 로딩 되지 않았습니다."),
                () -> assertEquals(testDate, releaseNoteContentResponseDTO.getReleaseDate(), () -> "릴리즈 노트 날짜가 제대로 로딩 되지 않았습니다.")
        );
    }

    @Test
    @Transactional
    @DisplayName("릴리즈 노트 수정 테스트")
    void updateReleaseNote() {
        User user = User.builder()
                .email("test@gmail.com")
                .name("Kim")
                .password("1q2w3e4r5t")
                .provider("local")
                .build();

        userRepository.saveAndFlush(user);

        request.setAttribute("id", user.getId());

        NoteBlock noteBlock = NoteBlock.builder()
                .label("new")
                .build();

        noteBlockRepository.save(noteBlock);

        ReleaseNote releaseNote = ReleaseNote.builder()
                .version("1.9.9")
                .lastModifiedDate(new Date())
                .releaseDate(new Date())
                .count(0)
                .isUpdated(false)
                .summary("test")
                .project(null)
                .noteBlockList(new ArrayList<>(Collections.singletonList(noteBlock)))
                .user(null)
                .commentList(null)
                .build();

        releaseNoteRepository.saveAndFlush(releaseNote);

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

        Date testDate = new Date();

        ReleaseNoteUpdateRequestDTO releaseNoteUpdateRequestDTO = new ReleaseNoteUpdateRequestDTO();
        releaseNoteUpdateRequestDTO.setVersion("2.9.9");
        releaseNoteUpdateRequestDTO.setReleaseDate(testDate);
        releaseNoteUpdateRequestDTO.setBlocks(noteBlockUpdateRequestDTOList);

        releaseNoteService.updateReleaseNote(request, releaseNote.getId() ,releaseNoteUpdateRequestDTO);

        ReleaseNote releaseNoteTest = releaseNoteRepository.findById(releaseNote.getId()).orElse(null);


        Assertions.assertAll(
                () -> assertEquals("update", releaseNoteTest.getNoteBlockList().get(0).getLabel(), () -> "릴리즈 노트 내용이 제대로 수정 되지 않았습니다."),
                () -> assertEquals("2.9.9", releaseNoteTest.getVersion(),() -> "릴리즈 노트 버전이 제대로 수정 되지 않았습니다."),
                () -> assertEquals(testDate, releaseNoteTest.getReleaseDate(), () -> "릴리즈 노트 날짜가 제대로 수정 되지 않았습니다.")
        );
    }

    @Test
    @Transactional(propagation = Propagation.SUPPORTS)
    @Rollback(value = true)
    @DisplayName("프로젝트 버전 리스트 로딩 테스트")
    void loadProjectVersionList() {
        User user = User.builder()
                .email("test@gmail.com")
                .name("Kim")
                .password("1q2w3e4r5t")
                .provider("local")
                .build();

        userRepository.saveAndFlush(user);

        request.setAttribute("id", user.getId());

        Project project = Project.builder()
                .createDate(new Date())
                .description("test project")
                .name("name")
                .build();

        projectRepository.saveAndFlush(project);

        UserInProject userInProject = UserInProject.builder()
                .user(user)
                .project(project)
                .role(UserRole.Developer)
                .build();

        userInProjectRepository.saveAndFlush(userInProject);

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

        Date testDate = new Date();
        ReleaseNoteCreateRequestDTO releaseNoteCreateRequestDTO = new ReleaseNoteCreateRequestDTO();
        releaseNoteCreateRequestDTO.setReleaseDate(testDate);
        releaseNoteCreateRequestDTO.setVersion("1.9.9");
        releaseNoteCreateRequestDTO.setBlocks(noteBlockCreateRequestDTOList);

        HttpResponse httpResponse = releaseNoteService.createReleaseNote(request, project.getId(), releaseNoteCreateRequestDTO);

        //ReleaseNote releaseNote = releaseNoteRepository.findById(Long.parseLong(httpResponse.getDescription().substring(18,21).replace(" ","").replace("C", ""))).orElse(null);

        //projectRepository.flush();
        //Project project1 = projectRepository.findById(project.getId()).orElse(null);

        ArrayList<ReleaseNoteVersionListResponseDTO> releaseNoteVersionListResponseDTO = releaseNoteService.loadProjectVersionList(request);


        Assertions.assertAll(
                () -> assertEquals("name", releaseNoteVersionListResponseDTO.get(0).getProjectName(), () -> "프로젝트 이름이이 제대로 로딩 되지 않았습니다."),
                () -> assertEquals("1.9.9", releaseNoteVersionListResponseDTO.get(0).getReleaseNoteVersionList().get(0).getVersion(),() -> "릴리즈 노트 버전이 제대로 로딩 되지 않았습니다.")
        );
    }

    @Test
    @Transactional
    @DisplayName("릴리즈 노트 삭제 테스트")
    void deleteReleaseNote() {
        ReleaseNote releaseNote = ReleaseNote.builder()
                .version("1.9.9")
                .lastModifiedDate(new Date())
                .releaseDate(new Date())
                .count(0)
                .isUpdated(false)
                .summary("test")
                .project(null)
                .noteBlockList(null)
                .user(null)
                .commentList(null)
                .build();

        releaseNoteRepository.saveAndFlush(releaseNote);

        releaseNoteService.deleteReleaseNote(releaseNote.getId());

        ReleaseNote releaseNoteTest = releaseNoteRepository.findById(releaseNote.getId()).orElse(null);

        Assertions.assertAll(
                () -> assertEquals(null, releaseNoteTest, () -> "릴리즈 노트가 제대로 삭제 되지 않았습니다.")
        );
    }

    @Test
    @Transactional
    @DisplayName("가장 최근 릴리즈 노트 로딩 테스트")
    void loadRecentReleaseNote() {
        User user = User.builder()
                .email("test@gmail.com")
                .name("Kim")
                .password("1q2w3e4r5t")
                .provider("local")
                .build();

        userRepository.saveAndFlush(user);

        request.setAttribute("id", user.getId());

        Project project = Project.builder()
                .createDate(new Date())
                .description("test project")
                .name("name")
                .build();

        projectRepository.saveAndFlush(project);

        UserInProject userInProject = UserInProject.builder()
                .user(user)
                .project(project)
                .role(UserRole.Developer)
                .build();

        userInProjectRepository.saveAndFlush(userInProject);

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

        Date testDate = new Date();
        ReleaseNoteCreateRequestDTO releaseNoteCreateRequestDTO = new ReleaseNoteCreateRequestDTO();
        releaseNoteCreateRequestDTO.setReleaseDate(testDate);
        releaseNoteCreateRequestDTO.setVersion("1.9.9");
        releaseNoteCreateRequestDTO.setBlocks(noteBlockCreateRequestDTOList);

        releaseNoteService.createReleaseNote(request, project.getId(), releaseNoteCreateRequestDTO);

        ReleaseNoteContentResponseDTO releaseNoteContentResponseDTO = releaseNoteService.loadRecentReleaseNote(request);

        Assertions.assertAll(
                () -> assertEquals("1.9.9", releaseNoteContentResponseDTO.getVersion(), () -> "릴리즈 노트 버전이 제대로 로딩 되지 않았습니다."),
                () -> assertEquals("Kim", releaseNoteContentResponseDTO.getCreator(), () -> "릴리즈 노트 작성자가 제대로 로딩 되지 않았습니다."),
                () -> assertEquals(testDate, releaseNoteContentResponseDTO.getReleaseDate(), () -> "릴리즈 노트 날짜가 제대로 로딩 되지 않았습니다."),
                () -> assertEquals("new", releaseNoteContentResponseDTO.getBlocks().get(0).getLabel(), () -> "릴리즈 노트 내용이 제대로 로딩 되지 않았습니다.")
        );
    }

    @Test
    @Transactional
    @DisplayName("조회수 증가 테스트")
    void increaseViewCount() {
        User user = User.builder()
                .email("test@gmail.com")
                .name("Kim")
                .password("1q2w3e4r5t")
                .provider("local")
                .build();

        userRepository.saveAndFlush(user);

        request.setAttribute("id", user.getId());

        Project project = Project.builder()
                .createDate(new Date())
                .description("test project")
                .name("name")
                .build();

        projectRepository.saveAndFlush(project);

        UserInProject userInProject = UserInProject.builder()
                .user(user)
                .project(project)
                .role(UserRole.Developer)
                .build();

        userInProjectRepository.saveAndFlush(userInProject);

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

        Date testDate = new Date();
        ReleaseNoteCreateRequestDTO releaseNoteCreateRequestDTO = new ReleaseNoteCreateRequestDTO();
        releaseNoteCreateRequestDTO.setReleaseDate(testDate);
        releaseNoteCreateRequestDTO.setVersion("1.9.9");
        releaseNoteCreateRequestDTO.setBlocks(noteBlockCreateRequestDTOList);

        HttpResponse httpResponse = releaseNoteService.createReleaseNote(request, project.getId(), releaseNoteCreateRequestDTO);
        releaseNoteService.loadReleaseNote(request, Long.parseLong(httpResponse.getDescription().substring(18,21).replace(" ","").replace("C", "")));
        ReleaseNoteContentResponseDTO releaseNoteContentResponseDTO = releaseNoteService.loadReleaseNote(request, Long.parseLong(httpResponse.getDescription().substring(18,21).replace(" ","").replace("C", "")));

        Assertions.assertAll(
                () -> assertEquals(1, releaseNoteContentResponseDTO.getCount(), () -> "조회수 증가가 제대로 되지 않았습니다.")
        );
    }

    @Test
    @Transactional
    @DisplayName("본 사람 체크 테스트")
    void seenCheck() {
        User user = User.builder()
                .email("test@gmail.com")
                .name("Kim")
                .password("1q2w3e4r5t")
                .provider("local")
                .build();

        userRepository.saveAndFlush(user);

        request.setAttribute("id", user.getId());

        Project project = Project.builder()
                .createDate(new Date())
                .description("test project")
                .name("name")
                .build();

        projectRepository.saveAndFlush(project);

        UserInProject userInProject = UserInProject.builder()
                .user(user)
                .project(project)
                .role(UserRole.Developer)
                .build();

        userInProjectRepository.saveAndFlush(userInProject);

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

        Date testDate = new Date();
        ReleaseNoteCreateRequestDTO releaseNoteCreateRequestDTO = new ReleaseNoteCreateRequestDTO();
        releaseNoteCreateRequestDTO.setReleaseDate(testDate);
        releaseNoteCreateRequestDTO.setVersion("1.9.9");
        releaseNoteCreateRequestDTO.setBlocks(noteBlockCreateRequestDTOList);

        HttpResponse httpResponse = releaseNoteService.createReleaseNote(request, project.getId(), releaseNoteCreateRequestDTO);
        releaseNoteService.loadReleaseNote(request, Long.parseLong(httpResponse.getDescription().substring(18,21).replace(" ","").replace("C", "")));

        SeenCheck seenCheck = seenCheckRepository.findByUserInProjectIdAndReleaseNoteId(userInProject.getId(), Long.parseLong(httpResponse.getDescription().substring(18,21).replace(" ","").replace("C", "")));

        Assertions.assertAll(
                () -> assertEquals("Kim", seenCheck.getUserName(), () -> "본 사람 체크가 제대로 되지 않았습니다.")
        );

    }
}