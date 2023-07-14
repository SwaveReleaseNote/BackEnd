package com.swave.urnr.ReleaseNote.service;

import com.swave.urnr.ChatGPT.service.ChatGPTService;
import com.swave.urnr.project.domain.Project;
import com.swave.urnr.project.repository.ProjectRepository;
import com.swave.urnr.ReleaseNote.RequestDTO.NewCommentDTO;
import com.swave.urnr.ReleaseNote.RequestDTO.NewReleaseNoteDTO;
import com.swave.urnr.ReleaseNote.RequestDTO.UpdateReleaseNoteDTO;
import com.swave.urnr.ReleaseNote.domain.Comment;
import com.swave.urnr.ReleaseNote.domain.NoteBlock;
import com.swave.urnr.ReleaseNote.domain.ReleaseNote;
import com.swave.urnr.ReleaseNote.repository.CommentRepository;
import com.swave.urnr.ReleaseNote.repository.NoteBlockRepository;
import com.swave.urnr.ReleaseNote.repository.ReleaseNoteRepository;
import com.swave.urnr.ReleaseNote.responseDTO.*;
import com.swave.urnr.user.domain.User;
import com.swave.urnr.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.transaction.Transactional;
import java.util.*;

@Service
@RequiredArgsConstructor
@EnableTransactionManagement
public class ReleaseNoteAllServiceImpl implements CommentService, NoteBlockService, ReleaseNoteService{

    private final CommentRepository commentRepository;
    private final NoteBlockRepository noteBlockRepository;
    private final ReleaseNoteRepository releaseNoteRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ChatGPTService chatGPTService;


    //todo : user의 정보를 세션에서 받아와서 user field를 채울 것 (세션/토큰 구현 후)
    @Override
    @Transactional
    public Long createReleaseNote(Long projectId, NewReleaseNoteDTO newReleaseNoteDTO) {
        Date currentDate = new Date();
        //SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");

        User user = userRepository.findById(1L) // test
                .orElseThrow(NoSuchElementException::new);

        Project project = projectRepository.findById(projectId)
                .orElseThrow(NoSuchElementException::new);

        //todo : 추후 noteBlock 제대로 구현시 create 방법을 고쳐야함
        NoteBlock noteBlock = NoteBlock.builder()
                .noteBlockContext(newReleaseNoteDTO.getContent())
                .build();


//        ChatGPTResultDTO chatGPTResultDTO =  chatGPTService.chatGptResult(
//                new ChatGPTQuestionRequestDTO(noteBlock.getNoteBlockContext() + "의 내용을 세줄로 요약해줘"));

        ReleaseNote releaseNote = ReleaseNote.builder()
                .version(newReleaseNoteDTO.getVersion())
                .lastModifiedDate(currentDate)
                .releaseDate(newReleaseNoteDTO.getReleaseDate())
                .count(0)
                .isUpdated(false)   //                .summary(chatGPTResultDTO.getText())
                .summary("Temp data until ChatGPT is OKAY")
                .project(project)
                .noteBlockList(new ArrayList<NoteBlock>(Collections.singletonList(noteBlock)))
                .user(user)
                .commentList(new ArrayList<Comment>())
                .build();

        noteBlock.setReleaseNote(releaseNote);
        releaseNoteRepository.save(releaseNote);
        noteBlockRepository.save(noteBlock);

        releaseNoteRepository.flush();
        noteBlockRepository.flush();

        return releaseNote.getId();
    }

    //project id를 받아서 해당 project에 연결된 모든 releaseNote를 리스트로 반환 -> 전체 출력용
    //todo : user의 정보를 세션에서 받아와서 user field를 채울 것 (세션/토큰 구현 후)
    //todo : noteBlock의 상세 구현법을 정한 이후에는 그것을 적용할 것 (현재는 단순히 글씨를 출력)
    @Override
    public ArrayList<ReleaseNoteContentListDTO> loadReleaseNoteList(Long projectId){
        List<ReleaseNote> releaseNoteList = releaseNoteRepository.findByProject_Id(projectId);

        ArrayList<ReleaseNoteContentListDTO> releaseNoteContentListDTOArrayList = new ArrayList<>();

        for(ReleaseNote releaseNote : releaseNoteList){
            releaseNoteContentListDTOArrayList.add(releaseNote.makeReleaseNoteContentListDTO());
        }

        return releaseNoteContentListDTOArrayList;
    }

    @Override
    public ReleaseNoteContentDTO loadReleaseNote(Long releaseNoteId){
        ReleaseNote releaseNote = releaseNoteRepository.findById(releaseNoteId)
                .orElseThrow(NoSuchElementException::new);

        ArrayList<Comment> commentList = commentRepository.findByReleaseNote_Id(releaseNoteId);
        ArrayList<CommentContentDTO> commentContentList = new ArrayList<>();

        for(Comment comment : commentList){
            CommentContentDTO commentContentDTO = new CommentContentDTO();
            commentContentDTO.setName(comment.getUser().getName());
            commentContentDTO.setContext(comment.getCommentContext());
            commentContentDTO.setLastModifiedDate(comment.getLastModifiedDate());

            commentContentList.add((commentContentDTO));
        }

        ReleaseNoteContentDTO releaseNoteContentDTO = releaseNote.makeReleaseNoteContentDTO();
        releaseNoteContentDTO.setComment(commentContentList);

        return releaseNoteContentDTO;
    }

    @Override
    public ReleaseNoteVersionListDTO loadVersionList(Long projectId){
        List<ReleaseNote> releaseNoteList = releaseNoteRepository.findByProject_Id(projectId);

        ReleaseNoteVersionListDTO releaseNoteVersionListDTO = new ReleaseNoteVersionListDTO();

        for(ReleaseNote releaseNote : releaseNoteList){
            releaseNoteVersionListDTO.getVersionList().add(releaseNote.getVersion());
        }

        return releaseNoteVersionListDTO;
    }

    //todo : user의 정보를 세션에서 받아와서 user field를 채울 것 (세션/토큰 구현 후)
    @Override
    @Transactional
    public Long createComment(NewCommentDTO newCommentDTO){

        User user = userRepository.findById(1L) // test
                .orElseThrow(NoSuchElementException::new);

        ReleaseNote releaseNote = releaseNoteRepository.findById(newCommentDTO.getReleaseNoteId())
                .orElseThrow(NoSuchElementException::new);

        Comment comment = Comment.builder()
                .user(user)
                .commentContext(newCommentDTO.getContent())
                .releaseNote(releaseNote)
                .lastModifiedDate(new Date())
                .build();

        releaseNote.getCommentList().add(comment);

        commentRepository.save(comment);
        releaseNoteRepository.save(releaseNote);

        commentRepository.flush();
        releaseNoteRepository.flush();

        return comment.getId();
    }

    //todo : user의 정보를 세션에서 받아와서 user field를 채울 것 (세션/토큰 구현 후)
    @Override
    @Transactional
    public Long updateReleaseNote(UpdateReleaseNoteDTO updateReleaseNoteDTO){
        User user = userRepository.findById(1L) // test
                .orElseThrow(NoSuchElementException::new);

        ReleaseNote releaseNote = releaseNoteRepository.findById(updateReleaseNoteDTO.getReleaseNoteId())
                .orElseThrow(NoSuchElementException::new);

        releaseNote.setVersion(updateReleaseNoteDTO.getVersion());
        releaseNote.setReleaseDate(updateReleaseNoteDTO.getReleaseDate());
        releaseNote.setLastModifiedDate(new Date());
        //todo : 추후 noteBlock 제대로 구현시 업데이트 방법을 고쳐야함
        releaseNote.getNoteBlockList().get(0).setNoteBlockContext(updateReleaseNoteDTO.getContent());
        releaseNote.setUser(user);
        releaseNote.setUpdated(true);

        return updateReleaseNoteDTO.getReleaseNoteId();
    };
    public CommentContentListDTO loadRecentComment(Long projectId){
        CommentContentListDTO commentContentListDTO = new CommentContentListDTO();


        return commentContentListDTO;
    }
}
