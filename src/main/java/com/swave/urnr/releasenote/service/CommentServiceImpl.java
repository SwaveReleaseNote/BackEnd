package com.swave.urnr.releasenote.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swave.urnr.releasenote.domain.Comment;
import com.swave.urnr.releasenote.domain.ReleaseNote;
import com.swave.urnr.releasenote.repository.CommentRepository;
import com.swave.urnr.releasenote.repository.ReleaseNoteRepository;
import com.swave.urnr.releasenote.requestdto.CommentCreateRequestDTO;
import com.swave.urnr.releasenote.responsedto.CommentContentResponseDTO;
import com.swave.urnr.releasenote.responsedto.CommentContentListResponseDTO;
import com.swave.urnr.user.domain.User;
import com.swave.urnr.user.domain.UserInProject;
import com.swave.urnr.user.repository.UserRepository;
import com.swave.urnr.util.http.HttpResponse;
import com.swave.urnr.util.kafka.KafkaSSEEmitterSentDTO;
import com.swave.urnr.util.kafka.KafkaService;
import com.swave.urnr.util.kafka.NotificationDTO;
import com.swave.urnr.util.kafka.NotificationEnum;
import com.swave.urnr.util.sse.SSETypeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
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

    private final KafkaService kafkaService;
    private ObjectMapper objectMapper = new ObjectMapper();
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


        /*
        comment : 작성자에게 알림이 갈 예정.
         */
            Long id = releaseNote.getUser().getId();


            /*
            TODO: Releasenote DTO 채우기  / 보내기
             */
            NotificationDTO notificationDTO= new NotificationDTO(NotificationEnum.COMMENT,new Date(System.currentTimeMillis()),releaseNote.getVersion(),releaseNoteId);

            // User ID에 message 보내주기
            kafkaService.produceMessage(notificationDTO,String.valueOf(id));
            // Emitter에 UserId 보내주기
            KafkaSSEEmitterSentDTO kafkaSSEEmitterSentDTO = new KafkaSSEEmitterSentDTO(String.valueOf(id),   releaseNote.getProject().getName()+" 프로젝트의 "+ releaseNote.getVersion()+" 버전 게시글에 "+ comment.getCommentContext() + " 댓글이 등록되었습니다.", SSETypeEnum.ALARM);
            try{
                String message = objectMapper.writeValueAsString(kafkaSSEEmitterSentDTO);
                kafkaService.produceMessageAsString(message, "emitter");
            }catch(Exception e){
                e.printStackTrace();
            }





        return HttpResponse.builder()
                .message("Comment Created")
                .description("Comment ID : " + comment.getId() + " Created")
                .build();
    }

    @Override
    @Transactional(readOnly = true)
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
    @Transactional(readOnly = true)
    public ArrayList<CommentContentResponseDTO> loadCommentList(Long releaseNoteId){
        ArrayList<Comment> commentList = commentRepository.findByReleaseNote_Id(releaseNoteId);
        ArrayList<CommentContentResponseDTO> commentContentList = new ArrayList<>();

        for(Comment comment : commentList){
            CommentContentResponseDTO commentContentResponseDTO = comment.makeCommentContentResponseDTO();
            commentContentList.add((commentContentResponseDTO));
        }

        return commentContentList;
    }
}
