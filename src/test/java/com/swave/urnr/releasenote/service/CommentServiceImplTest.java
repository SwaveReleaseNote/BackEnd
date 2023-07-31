package com.swave.urnr.releasenote.service;

import com.swave.urnr.project.domain.Project;
import com.swave.urnr.project.repository.ProjectRepository;
import com.swave.urnr.releasenote.domain.Comment;
import com.swave.urnr.releasenote.domain.ReleaseNote;
import com.swave.urnr.releasenote.repository.CommentRepository;
import com.swave.urnr.releasenote.repository.ReleaseNoteRepository;
import com.swave.urnr.releasenote.requestdto.CommentCreateRequestDTO;
import com.swave.urnr.releasenote.responsedto.CommentContentListResponseDTO;
import com.swave.urnr.releasenote.responsedto.CommentContentResponseDTO;
import com.swave.urnr.user.domain.User;
import com.swave.urnr.user.repository.UserRepository;
import com.swave.urnr.util.http.HttpResponse;
import com.swave.urnr.util.querydsl.QueryDslConfig;
import org.junit.jupiter.api.*;
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
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Import(QueryDslConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CommentServiceImplTest {

    @Autowired
    private CommentService commentService;
    @Autowired
    private ReleaseNoteRepository releaseNoteRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ProjectRepository projectRepository;

    private MockHttpServletRequest request;
    private ReleaseNote releaseNote;
    private CommentCreateRequestDTO commentCreateRequestDTO;


    @BeforeEach
    void setUp() {
        MockHttpSession httpSession = new MockHttpSession();
        request = new MockHttpServletRequest();
        request.setSession(httpSession);
        request.setAttribute("username", "Kim");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @Test
    @Transactional
    @DisplayName("댓글 생성 테스트")
    void createComment() {
        User user = User.builder()
                .email("test@gmail.com")
                .name("Kim")
                .password("1q2w3e4r5t")
                .provider("local")
                .build();

        userRepository.saveAndFlush(user);

        request.setAttribute("id", user.getId());

        releaseNote = ReleaseNote.builder()
                .version("1.0.0")
                .lastModifiedDate(new Date())
                .releaseDate(new Date())
                .count(0)
                .isUpdated(false)
                .summary("summary")
                .project(projectRepository.findById(1L).orElse(null))
                .noteBlockList(null)
                .user(null)
                .commentList(new ArrayList<>())
                .build();

        releaseNoteRepository.saveAndFlush(releaseNote);

        commentCreateRequestDTO = new CommentCreateRequestDTO();
        commentCreateRequestDTO.setContent("test");

        HttpResponse httpResponse = commentService.createComment(request, releaseNote.getId(),commentCreateRequestDTO);
        Comment comment = commentRepository.findById(Long.parseLong(httpResponse.getDescription().substring(13,16).replace(" ","").replace("C", "")))
                .orElseThrow(NoSuchElementException::new);

        Assertions.assertAll(
                    () -> assertEquals(releaseNote.getId(), comment.getReleaseNote().getId(), () -> "댓글의 매핑이 제대로 되지 않았습니다"),
                    () -> assertEquals("test", comment.getCommentContext(),() -> "댓글 내용이 제대로 생성 되지 않았습니다.")
                );
    }

    @Test
    @Transactional
    @DisplayName("최근 댓글 로드 테스트")
    void loadRecentComment() {

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

        releaseNote = ReleaseNote.builder()
                .version("1.0.0")
                .lastModifiedDate(new Date())
                .releaseDate(new Date())
                .count(0)
                .isUpdated(false)
                .summary("summary")
                .project(project)
                .noteBlockList(null)
                .user(null)
                .commentList(new ArrayList<>())
                .build();

        releaseNoteRepository.saveAndFlush(releaseNote);

        commentCreateRequestDTO = new CommentCreateRequestDTO();
        commentCreateRequestDTO.setContent("test");

        commentService.createComment(request, releaseNote.getId(),commentCreateRequestDTO);

        CommentContentListResponseDTO commentContentListResponseDTO = commentService.loadRecentComment(project.getId());

        Assertions.assertAll(
                () -> assertEquals("test", commentContentListResponseDTO.getComments().get(0).getContext(), () -> "댓글의 로드가 제대로 되지 않았습니다")
        );

    }

    @Test
    @Transactional
    @DisplayName("댓글 삭제 테스트")
    void deleteComment() {

        User user = User.builder()
                .email("test@gmail.com")
                .name("Kim")
                .password("1q2w3e4r5t")
                .provider("local")
                .build();

        userRepository.saveAndFlush(user);

        request.setAttribute("id", user.getId());

        releaseNote = ReleaseNote.builder()
                .version("1.0.0")
                .lastModifiedDate(new Date())
                .releaseDate(new Date())
                .count(0)
                .isUpdated(false)
                .summary("summary")
                .project(projectRepository.findById(1L).orElse(null))
                .noteBlockList(null)
                .user(null)
                .commentList(new ArrayList<>())
                .build();

        releaseNoteRepository.saveAndFlush(releaseNote);

        commentCreateRequestDTO = new CommentCreateRequestDTO();
        commentCreateRequestDTO.setContent("test");

        HttpResponse httpResponse = commentService.createComment(request, releaseNote.getId(),commentCreateRequestDTO);

        Long commentId = Long.parseLong(httpResponse.getDescription().substring(13,16).replace(" ","").replace("C", ""));
        commentService.deleteComment(commentId);

        Comment comment = commentRepository.findById(commentId).orElse(null);
        Assertions.assertAll(
                () -> assertEquals(null, comment, () -> "댓글이 제대로 삭제 되지 않았습니다")
        );

    }

    @Test
    @Transactional
    @DisplayName("댓글 리스트 로드 테스트")
    void loadCommentList(){

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

        releaseNote = ReleaseNote.builder()
                .version("1.0.0")
                .lastModifiedDate(new Date())
                .releaseDate(new Date())
                .count(0)
                .isUpdated(false)
                .summary("summary")
                .project(project)
                .noteBlockList(null)
                .user(user)
                .commentList(new ArrayList<>())
                .build();

        releaseNoteRepository.saveAndFlush(releaseNote);

        commentCreateRequestDTO = new CommentCreateRequestDTO();
        commentCreateRequestDTO.setContent("test");

        commentService.createComment(request, releaseNote.getId(),commentCreateRequestDTO);

        ArrayList<CommentContentResponseDTO> commentContentResponseDTO = commentService.loadCommentList(releaseNote.getId());

        Assertions.assertAll(
                () -> assertEquals("test", commentContentResponseDTO.get(0).getContext(), () -> "댓글의 로드가 제대로 되지 않았습니다")
        );
    }

}