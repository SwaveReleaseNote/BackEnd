package com.swave.urnr.releasenote.service;

import com.swave.urnr.project.domain.Project;
import com.swave.urnr.project.repository.ProjectRepository;
import com.swave.urnr.releasenote.domain.NoteBlock;
import com.swave.urnr.releasenote.domain.ReleaseNote;
import com.swave.urnr.releasenote.repository.NoteBlockRepository;
import com.swave.urnr.releasenote.repository.ReleaseNoteRepository;
import com.swave.urnr.releasenote.requestdto.BlockContextCreateRequestDTO;
import com.swave.urnr.releasenote.requestdto.NoteBlockCreateRequestDTO;
import com.swave.urnr.releasenote.requestdto.NoteBlockUpdateRequestDTO;
import com.swave.urnr.releasenote.requestdto.ReleaseNoteCreateRequestDTO;
import com.swave.urnr.releasenote.responsedto.ReleaseNoteContentResponseDTO;
import com.swave.urnr.releasenote.responsedto.ReleaseNoteLabelContentResponseDTO;
import com.swave.urnr.releasenote.responsedto.ReleaseNoteLabelCountResponseDTO;
import com.swave.urnr.user.domain.User;
import com.swave.urnr.user.domain.UserInProject;
import com.swave.urnr.user.repository.UserInProjectRepository;
import com.swave.urnr.user.repository.UserRepository;
import com.swave.urnr.util.http.HttpResponse;
import com.swave.urnr.util.querydsl.QueryDslConfig;
import com.swave.urnr.util.type.UserRole;
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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import(QueryDslConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class NoteBlockServiceTest {

    @Autowired
    private ReleaseNoteService releaseNoteService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserInProjectRepository userInProjectRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private NoteBlockRepository noteBlockRepository;
    @Autowired
    private ReleaseNoteRepository releaseNoteRepository;

    @Autowired
    private NoteBlockService noteBlockService;

    private MockHttpServletRequest request;

    @BeforeEach
    void setUp(){
        MockHttpSession httpSession = new MockHttpSession();
        request = new MockHttpServletRequest();
        request.setSession(httpSession);
        request.setAttribute("id", 1L);
        request.setAttribute("username", "Kim");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @Test
    @DisplayName(value = "노트 블럭 생성 테스트")
    @Transactional
    void createNoteBlock(){
        NoteBlockCreateRequestDTO noteBlockCreateRequestDTO = new NoteBlockCreateRequestDTO();
        noteBlockCreateRequestDTO.setLabel("new");

        ReleaseNote releaseNote = new ReleaseNote();
        releaseNoteRepository.save(releaseNote);

        NoteBlock noteBlock = noteBlockService.createNoteBlock(noteBlockCreateRequestDTO,releaseNote);

        NoteBlock noteBlock1 = noteBlockRepository.findById(noteBlock.getId())
                .orElseThrow(NoSuchElementException::new);

        Assertions.assertAll(
                () -> assertEquals("new", noteBlock1.getLabel(), () -> "노트 블럭 생성이 제대로 되지 않았습니다.")
        );
    }

    @Test
    @DisplayName(value = "노트 블럭 수정 테스트")
    @Transactional
    void updateNoteBlock(){
        NoteBlockCreateRequestDTO noteBlockCreateRequestDTO = new NoteBlockCreateRequestDTO();
        noteBlockCreateRequestDTO.setLabel("new");

        ReleaseNote releaseNote = new ReleaseNote();
        releaseNoteRepository.save(releaseNote);

        NoteBlock noteBlock = noteBlockService.createNoteBlock(noteBlockCreateRequestDTO,releaseNote);

        NoteBlockUpdateRequestDTO noteBlockUpdateRequestDTO = new NoteBlockUpdateRequestDTO();
        noteBlockUpdateRequestDTO.setLabel("update");

        NoteBlock noteBlock1 = noteBlockService.updateNoteBlock(noteBlockUpdateRequestDTO,releaseNote);

        NoteBlock noteBlock2 = noteBlockRepository.findById(noteBlock1.getId())
                .orElseThrow(NoSuchElementException::new);

        Assertions.assertAll(
                () -> assertEquals("update", noteBlock2.getLabel(), () -> "노트 블럭 업데이트가 제대로 되지 않았습니다.")
        );

    }

    @Test
    @DisplayName(value = "노트 블럭 삭제 테스트")
    @Transactional
    void deleteNoteBlock(){
        NoteBlockCreateRequestDTO noteBlockCreateRequestDTO = new NoteBlockCreateRequestDTO();
        noteBlockCreateRequestDTO.setLabel("new");

        ReleaseNote releaseNote = new ReleaseNote();
        releaseNoteRepository.save(releaseNote);

        NoteBlock noteBlock = noteBlockService.createNoteBlock(noteBlockCreateRequestDTO,releaseNote);

        NoteBlockUpdateRequestDTO noteBlockUpdateRequestDTO = new NoteBlockUpdateRequestDTO();
        noteBlockUpdateRequestDTO.setLabel("update");

        noteBlockService.deleteNoteBlock(noteBlock.getId());

        NoteBlock noteBlock1 = noteBlockRepository.findById(noteBlock.getId())
                .orElse(null);

        Assertions.assertAll(
                () -> assertEquals(null, noteBlock1, () -> "노트 블럭 삭제가 제대로 되지 않았습니다.")
        );
    }

    @Test
    @DisplayName(value = "라벨 카운팅 테스트")
    @Transactional
    void countLabel() {
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

        List<NoteBlockCreateRequestDTO> noteBlockCreateRequestDTOList = new ArrayList<>();

        for(String label : List.of("new","update","new","update")) {

            List<BlockContextCreateRequestDTO> blockContextContentResponseDTOList = new ArrayList<>();
            for(String context : List.of("test, ", " test")) {
                BlockContextCreateRequestDTO blockContextCreateRequestDTO = new BlockContextCreateRequestDTO();

                blockContextCreateRequestDTO.setContext(context);
                blockContextCreateRequestDTO.setTag("H1");
                blockContextCreateRequestDTO.setIndex(1L);

                blockContextContentResponseDTOList.add(blockContextCreateRequestDTO);
            }

            NoteBlockCreateRequestDTO noteBlockCreateRequestDTO = new NoteBlockCreateRequestDTO();
            noteBlockCreateRequestDTO.setLabel(label);
            noteBlockCreateRequestDTO.setContexts(blockContextContentResponseDTOList);

            noteBlockCreateRequestDTOList.add(noteBlockCreateRequestDTO);
        }

        Date testDate = new Date();
        ReleaseNoteCreateRequestDTO releaseNoteCreateRequestDTO = new ReleaseNoteCreateRequestDTO();
        releaseNoteCreateRequestDTO.setReleaseDate(testDate);
        releaseNoteCreateRequestDTO.setVersion("1.9.9");
        releaseNoteCreateRequestDTO.setBlocks(noteBlockCreateRequestDTOList);

        releaseNoteService.createReleaseNote(request, project.getId(), releaseNoteCreateRequestDTO);

        List<ReleaseNoteLabelCountResponseDTO> releaseNoteLabelCountResponseDTOList = noteBlockService.countLabel(project.getId());

        Assertions.assertAll(
                () -> assertEquals(2, releaseNoteLabelCountResponseDTOList.toArray().length, () -> "라벨 그룹핑이 제대로 되지 않았습니다."),
                () -> assertEquals(2, releaseNoteLabelCountResponseDTOList.get(0).getCount(),() -> "라벨 카운팅이 제대로 되지 않았습니다.")
        );
    }

    @Test
    @DisplayName(value = "라벨 필터 테스트")
    @Transactional
    void filterByLabel() {
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

        List<NoteBlockCreateRequestDTO> noteBlockCreateRequestDTOList = new ArrayList<>();

        for(String label : List.of("new","update","new","update")) {

            List<BlockContextCreateRequestDTO> blockContextContentResponseDTOList = new ArrayList<>();
            for(String context : List.of("test, ", " test")) {
                BlockContextCreateRequestDTO blockContextCreateRequestDTO = new BlockContextCreateRequestDTO();

                blockContextCreateRequestDTO.setContext(context);
                blockContextCreateRequestDTO.setTag("H1");
                blockContextCreateRequestDTO.setIndex(1L);

                blockContextContentResponseDTOList.add(blockContextCreateRequestDTO);
            }

            NoteBlockCreateRequestDTO noteBlockCreateRequestDTO = new NoteBlockCreateRequestDTO();
            noteBlockCreateRequestDTO.setLabel(label);
            noteBlockCreateRequestDTO.setContexts(blockContextContentResponseDTOList);

            noteBlockCreateRequestDTOList.add(noteBlockCreateRequestDTO);
        }

        Date testDate = new Date();
        ReleaseNoteCreateRequestDTO releaseNoteCreateRequestDTO = new ReleaseNoteCreateRequestDTO();
        releaseNoteCreateRequestDTO.setReleaseDate(testDate);
        releaseNoteCreateRequestDTO.setVersion("1.9.9");
        releaseNoteCreateRequestDTO.setBlocks(noteBlockCreateRequestDTOList);

        releaseNoteService.createReleaseNote(request, project.getId(), releaseNoteCreateRequestDTO);

        List<ReleaseNoteLabelContentResponseDTO> releaseNoteLabelContentResponseDTOList  = noteBlockService.filterByLabel(project.getId());

        Assertions.assertAll(
                () -> assertEquals(4, releaseNoteLabelContentResponseDTOList.toArray().length, () -> "라벨 로딩이 제대로 되지 않았습니다."),
                () -> assertEquals("1.9.9", releaseNoteLabelContentResponseDTOList.get(0).getVersion(),() -> "릴리즈 노트 버전 로딩이 제대로 되지 않았습니다.")
        );

    }

}