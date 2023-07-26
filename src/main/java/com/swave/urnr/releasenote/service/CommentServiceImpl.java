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

        commentRepository.saveAndFlush(comment);

        releaseNote.getCommentList().add(comment);
        releaseNoteRepository.save(releaseNote);

        return HttpResponse.builder()
                .message("Comment Created")
                .description("Comment ID : " + comment.getId() + " Created")
                .build();
    }

    @Override
    public CommentContentListResponseDTO loadRecentComment(Long projectId){
        CommentContentListResponseDTO commentContentListResponseDTO = new CommentContentListResponseDTO(new ArrayList<>());
        List<CommentContentResponseDTO> comments = commentRepository.findTop5RecentComment(projectId);

        for(CommentContentResponseDTO comment : comments) {
            commentContentListResponseDTO.getComments().add(comment);
        }

        return commentContentListResponseDTO;
    }

    @Override
    @Transactional
    public HttpResponse deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);

        return HttpResponse.builder()
                .message("Comment Deleted")
                .description("Comment ID : " + commentId + " Deleted")
                .build();
    }

    @Override
    public ArrayList<CommentContentResponseDTO> loadCommentList(Long releaseNoteId){
        ArrayList<Comment> commentList = commentRepository.findByReleaseNote_Id(releaseNoteId);
        ArrayList<CommentContentResponseDTO> commentContentList = new ArrayList<>();

        for(Comment comment : commentList){
            CommentContentResponseDTO commentContentResponseDTO = new CommentContentResponseDTO();
            commentContentResponseDTO.setName(comment.getUser().getUsername());
            commentContentResponseDTO.setContext(comment.getCommentContext());
            commentContentResponseDTO.setLastModifiedDate(comment.getLastModifiedDate());

            commentContentList.add((commentContentResponseDTO));
        }

        return commentContentList;
    }
}
