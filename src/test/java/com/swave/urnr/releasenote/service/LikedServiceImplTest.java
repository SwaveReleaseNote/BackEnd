package com.swave.urnr.releasenote.service;

import com.swave.urnr.project.domain.Project;
import com.swave.urnr.project.repository.ProjectRepository;
import com.swave.urnr.releasenote.domain.Liked;
import com.swave.urnr.releasenote.domain.ReleaseNote;
import com.swave.urnr.releasenote.domain.SeenCheck;
import com.swave.urnr.releasenote.repository.LikedRepository;
import com.swave.urnr.releasenote.repository.ReleaseNoteRepository;
import com.swave.urnr.releasenote.repository.SeenCheckRepository;
import com.swave.urnr.user.domain.User;
import com.swave.urnr.user.domain.UserInProject;
import com.swave.urnr.user.repository.UserInProjectRepository;
import com.swave.urnr.user.repository.UserRepository;
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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import(QueryDslConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class LikedServiceImplTest {

    @Autowired
    private ReleaseNoteRepository releaseNoteRepository;
    @Autowired
    private UserInProjectRepository userInProjectRepository;
    @Autowired
    private SeenCheckRepository seenCheckRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private LikedRepository likedRepository;

    @Autowired
    private LikedService likedService;
    @Autowired
    private ReleaseNoteService releaseNoteService;

    private MockHttpServletRequest request;

    @BeforeEach
    void setUp() {
        MockHttpSession httpSession = new MockHttpSession();
        request = new MockHttpServletRequest();
        request.setSession(httpSession);
        request.setAttribute("username", "Kim");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @Test
    @DisplayName("좋아요 생성 테스트")
    @Transactional
    void createLiked() {
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
                .project(project)
                .role(UserRole.Developer)
                .user(user)
                .build();

        userInProjectRepository.saveAndFlush(userInProject);

        ReleaseNote releaseNote = ReleaseNote.builder()
                .version("1.9.9")
                .lastModifiedDate(new Date())
                .releaseDate(new Date())
                .count(0)
                .isUpdated(false)
                .summary("test")
                .project(project)
                .noteBlockList(new ArrayList())
                .user(user)
                .commentList(new ArrayList())
                .build();

        releaseNoteRepository.save(releaseNote);

        releaseNoteService.loadReleaseNote(request, releaseNote.getId());

        SeenCheck seenCheck = seenCheckRepository.findByUserInProjectIdAndReleaseNoteId(userInProject.getId(), releaseNote.getId());

        likedService.createLiked(request, releaseNote.getId());

        Liked liked = likedRepository.findBySeenCheck_Id(seenCheck.getId());
        Assertions.assertAll(
                () -> assertNotEquals(null, liked,() -> "좋아요가 제대로 생성 되지 않았습니다."),
                () -> assertEquals(true, liked.getIsLiked(), () -> "좋아요의 내용이 제대로 생성되지 않았습니다.")
        );
    }

    @Test
    @DisplayName("좋아요 취소 테스트")
    @Transactional
    void cancelLiked() {
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
                .project(project)
                .role(UserRole.Developer)
                .user(user)
                .build();

        userInProjectRepository.saveAndFlush(userInProject);

        ReleaseNote releaseNote = ReleaseNote.builder()
                .version("1.9.9")
                .lastModifiedDate(new Date())
                .releaseDate(new Date())
                .count(0)
                .isUpdated(false)
                .summary("test")
                .project(project)
                .noteBlockList(new ArrayList())
                .user(user)
                .commentList(new ArrayList())
                .build();

        releaseNoteRepository.save(releaseNote);

        releaseNoteService.loadReleaseNote(request, releaseNote.getId());

        SeenCheck seenCheck = seenCheckRepository.findByUserInProjectIdAndReleaseNoteId(userInProject.getId(), releaseNote.getId());

        likedService.createLiked(request, releaseNote.getId());

        likedService.cancelLiked(request, releaseNote.getId());

        Liked liked = likedRepository.findBySeenCheck_Id(seenCheck.getId());
        Assertions.assertAll(
                () -> assertEquals(false, liked.getIsLiked(), () -> "좋아요가 제대로 취소되지 않았습니다.")
        );
    }

    @Test
    @DisplayName("좋아요 카운트 테스트")
    @Transactional
    void countLiked() {
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
                .project(project)
                .role(UserRole.Developer)
                .user(user)
                .build();

        userInProjectRepository.saveAndFlush(userInProject);

        ReleaseNote releaseNote = ReleaseNote.builder()
                .version("1.9.9")
                .lastModifiedDate(new Date())
                .releaseDate(new Date())
                .count(0)
                .isUpdated(false)
                .summary("test")
                .project(project)
                .noteBlockList(new ArrayList())
                .user(user)
                .commentList(new ArrayList())
                .build();

        releaseNoteRepository.save(releaseNote);

        releaseNoteService.loadReleaseNote(request, releaseNote.getId());

        likedService.createLiked(request, releaseNote.getId());

        SeenCheck seenCheck = SeenCheck.builder()
                .releaseNote(releaseNote)
                .userName("lee")
                .build();

        seenCheckRepository.save(seenCheck);

        likedRepository.saveAndFlush(Liked.builder()
                .isLiked(true)
                .seenCheck(seenCheck)
                .build());

        Long count = likedRepository.countByLiked(releaseNote.getId());
        Assertions.assertAll(
                () -> assertEquals(2, count, () -> "좋아요가 제대로 카운팅되지 않았습니다.")
        );
    }
}