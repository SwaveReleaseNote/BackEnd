package com.swave.urnr.releasenote.service;

import com.swave.urnr.chatgpt.service.ChatGPTService;
import com.swave.urnr.project.domain.Project;
import com.swave.urnr.project.repository.ProjectRepository;
import com.swave.urnr.releasenote.requestdto.ReleaseNoteCreateRequestDTO;
import com.swave.urnr.releasenote.requestdto.ReleaseNoteUpdateRequestDTO;
import com.swave.urnr.releasenote.domain.Comment;
import com.swave.urnr.releasenote.domain.NoteBlock;
import com.swave.urnr.releasenote.domain.ReleaseNote;
import com.swave.urnr.releasenote.repository.CommentRepository;
import com.swave.urnr.releasenote.repository.NoteBlockRepository;
import com.swave.urnr.releasenote.repository.ReleaseNoteRepository;
import com.swave.urnr.releasenote.responsedto.*;
import com.swave.urnr.user.domain.User;
import com.swave.urnr.user.domain.UserInProject;
import com.swave.urnr.user.repository.UserInProjectRepository;
import com.swave.urnr.user.repository.UserRepository;
import com.swave.urnr.util.http.HttpResponse;
import com.swave.urnr.util.type.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
@EnableTransactionManagement
public class ReleaseNoteServiceImpl implements NoteBlockService, ReleaseNoteService{

    private final CommentRepository commentRepository;
    private final NoteBlockRepository noteBlockRepository;
    private final ReleaseNoteRepository releaseNoteRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final UserInProjectRepository userInProjectRepository;
    private final ChatGPTService chatGPTService;

    @Override
    @Transactional
    public HttpResponse createReleaseNote(HttpServletRequest request, Long projectId, ReleaseNoteCreateRequestDTO releaseNoteCreateRequestDTO) {
        Date currentDate = new Date();
        //SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");

        User user = userRepository.findById((Long) request.getAttribute("id"))
                .orElseThrow(NoSuchElementException::new);

        Project project = projectRepository.findById(projectId)
                .orElseThrow(NoSuchElementException::new);

        //todo : 추후 noteBlock 제대로 구현시 create 방법을 고쳐야함
        NoteBlock noteBlock = NoteBlock.builder()
                .noteBlockContext(releaseNoteCreateRequestDTO.getContent())
                .build();


//        ChatGPTResultDTO chatGPTResultDTO =  chatGPTService.chatGptResult(
//                new ChatGPTQuestionRequestDTO(noteBlock.getNoteBlockContext() + "의 내용을 세줄로 요약해줘"));

        ReleaseNote releaseNote = ReleaseNote.builder()
                .version(releaseNoteCreateRequestDTO.getVersion())
                .lastModifiedDate(currentDate)
                .releaseDate(releaseNoteCreateRequestDTO.getReleaseDate())
                .count(0)
                .isUpdated(false)
//              .summary(chatGPTResultDTO.getText())
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

        return HttpResponse.builder()
                .message("Release Note Created")
                .description("Release Note ID : " + releaseNote.getId() + " Created")
                .build();
    }

    //project id를 받아서 해당 project에 연결된 모든 releaseNote를 리스트로 반환 -> 전체 출력용
    //todo : noteBlock의 상세 구현법을 정한 이후에는 그것을 적용할 것 (현재는 단순히 글씨를 출력)
    @Override
    public ArrayList<ReleaseNoteContentListResponseDTO> loadReleaseNoteList(Long projectId){
        List<ReleaseNote> releaseNoteList = releaseNoteRepository.findByProject_Id(projectId);

        ArrayList<ReleaseNoteContentListResponseDTO> releaseNoteContentListDTOArrayListResponse = new ArrayList<>();

        for(ReleaseNote releaseNote : releaseNoteList){
            releaseNoteContentListDTOArrayListResponse.add(releaseNote.makeReleaseNoteContentListDTO());
        }

        return releaseNoteContentListDTOArrayListResponse;
    }

    @Override
    public ReleaseNoteContentResponseDTO loadReleaseNote(Long releaseNoteId){
        ReleaseNote releaseNote = releaseNoteRepository.findById(releaseNoteId)
                .orElseThrow(NoSuchElementException::new);

        ArrayList<Comment> commentList = commentRepository.findByReleaseNote_Id(releaseNoteId);
        ArrayList<CommentContentResponseDTO> commentContentList = new ArrayList<>();

        for(Comment comment : commentList){
            CommentContentResponseDTO commentContentResponseDTO = new CommentContentResponseDTO();
            commentContentResponseDTO.setName(comment.getUser().getUsername());
            commentContentResponseDTO.setContext(comment.getCommentContext());
            commentContentResponseDTO.setLastModifiedDate(comment.getLastModifiedDate());

            commentContentList.add((commentContentResponseDTO));
        }

        ReleaseNoteContentResponseDTO releaseNoteContentResponseDTO = releaseNote.makeReleaseNoteContentDTO();
        releaseNoteContentResponseDTO.setComment(commentContentList);

        return releaseNoteContentResponseDTO;
    }

    //todo : user의 정보를 세션에서 받아와서 user field를 채울 것 (세션/토큰 구현 후)
    @Override
    @Transactional
    public HttpResponse updateReleaseNote(HttpServletRequest request, ReleaseNoteUpdateRequestDTO releaseNoteUpdateRequestDTO){
        User user = userRepository.findById((Long) request.getAttribute("id"))
                .orElseThrow(NoSuchElementException::new);

        ReleaseNote releaseNote = releaseNoteRepository.findById(releaseNoteUpdateRequestDTO.getReleaseNoteId())
                .orElseThrow(NoSuchElementException::new);

        releaseNote.setVersion(releaseNoteUpdateRequestDTO.getVersion());
        releaseNote.setReleaseDate(releaseNoteUpdateRequestDTO.getReleaseDate());
        releaseNote.setLastModifiedDate(new Date());
        //todo : 추후 noteBlock 제대로 구현시 업데이트 방법을 고쳐야함
        releaseNote.getNoteBlockList().get(0).setNoteBlockContext(releaseNoteUpdateRequestDTO.getContent());
        releaseNote.setUser(user);
        releaseNote.setUpdated(true);

        return HttpResponse.builder()
                .message("Release Note Updated")
                .description("Release Note ID : " + releaseNote.getId() + " Updated")
                .build();
    }

    @Override
    public ArrayList<ReleaseNoteVersionListResponseDTO> loadProjectVersionList(HttpServletRequest request){
        ArrayList<ReleaseNoteVersionListResponseDTO> responseVersionListDTOList = new ArrayList<>();
        List<UserInProject> userInProjectList = userInProjectRepository.findByUser_Id((Long) request.getAttribute("id"));

        for(UserInProject userInProject : userInProjectList){
            ReleaseNoteVersionListResponseDTO releaseNoteVersionListResponseDTO = new ReleaseNoteVersionListResponseDTO();

            releaseNoteVersionListResponseDTO.setProjectId(userInProject.getProject().getId());
            releaseNoteVersionListResponseDTO.setProjectName(userInProject.getProject().getName());
            releaseNoteVersionListResponseDTO.setSubscribe(UserRole.Subscriber == userInProject.getRole());

            List<ReleaseNote> releaseNoteList = userInProject.getProject().getReleaseNoteList();
            ArrayList<ReleaseNoteVersionResponseDTO> releaseNoteVersionListDTOListResponse = new ArrayList<>();

            for(ReleaseNote releaseNote : releaseNoteList){
                ReleaseNoteVersionResponseDTO releaseNoteVersionResponseDTO = new ReleaseNoteVersionResponseDTO();

                releaseNoteVersionResponseDTO.setReleaseNoteId(releaseNote.getId());
                releaseNoteVersionResponseDTO.setVersion(releaseNote.getVersion());

                releaseNoteVersionListDTOListResponse.add(releaseNoteVersionResponseDTO);
            }
            releaseNoteVersionListResponseDTO.setReleaseNoteVersionList(releaseNoteVersionListDTOListResponse);

            responseVersionListDTOList.add(releaseNoteVersionListResponseDTO);
        }
        return responseVersionListDTOList;
    };

    @Override
    public HttpResponse deleteReleaseNote(Long releaseNoteId){
        releaseNoteRepository.deleteById(releaseNoteId);

        return HttpResponse.builder()
                .message("Release Note Deleted")
                .description("Release Note ID : " + releaseNoteId + " Deleted")
                .build();
    }
}
