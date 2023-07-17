package com.swave.urnr.releasenote.service;

import com.swave.urnr.releasenote.domain.Comment;
import com.swave.urnr.releasenote.domain.ReleaseNote;
import com.swave.urnr.releasenote.repository.CommentRepository;
import com.swave.urnr.releasenote.repository.ReleaseNoteRepository;
import com.swave.urnr.releasenote.requestdto.CommentCreateRequestDTO;
import com.swave.urnr.releasenote.responsedto.CommentContentResponseDTO;
import com.swave.urnr.releasenote.responsedto.CommentContentListResponseDTO;
import com.swave.urnr.user.domain.User;
import com.swave.urnr.user.repository.UserRepository;
import com.swave.urnr.util.http.HttpResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;


@Service
@RequiredArgsConstructor
@Slf4j
@EnableTransactionManagement
public class CommentServiceImpl implements CommentService {

    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final ReleaseNoteRepository releaseNoteRepository;
    @Override
    @Transactional
    public HttpResponse createComment(HttpServletRequest request, Long releaseNoteId , CommentCreateRequestDTO commentCreateRequestDTO){

        User user = userRepository.findById((Long) request.getAttribute("id"))
                .orElseThrow(NoSuchElementException::new);

        ReleaseNote releaseNote = releaseNoteRepository.findById(releaseNoteId)
                .orElseThrow(NoSuchElementException::new);

        Comment comment = Comment.builder()
                .user(user)
                .commentContext(commentCreateRequestDTO.getContent())
                .releaseNote(releaseNote)
                .lastModifiedDate(new Date())
                .build();

        releaseNote.getCommentList().add(comment);

        commentRepository.save(comment);
        releaseNoteRepository.save(releaseNote);

        commentRepository.flush();
        releaseNoteRepository.flush();

        return HttpResponse.builder()
                .message("Comment Created")
                .description("Comment ID : " + releaseNote.getId() + " Created")
                .build();
    }

    public CommentContentListResponseDTO loadRecentComment(Long projectId){
        CommentContentListResponseDTO commentContentListResponseDTO = new CommentContentListResponseDTO(new ArrayList<>());
        List<Comment> comments = commentRepository.findTop5RecentComment(projectId);

        for(Comment comment : comments) {
            CommentContentResponseDTO commentContentResponseDTO = new CommentContentResponseDTO();

            commentContentResponseDTO.setName(comment.getUser().getUsername());
            commentContentResponseDTO.setContext(comment.getCommentContext());
            commentContentResponseDTO.setLastModifiedDate(comment.getLastModifiedDate());

            commentContentListResponseDTO.getComments().add(commentContentResponseDTO);
        }

        return commentContentListResponseDTO;
    }

    @Override
    public HttpResponse deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);

        return HttpResponse.builder()
                .message("Comment Deleted")
                .description("Comment ID : " + commentId + " Deleted")
                .build();
    }
}
