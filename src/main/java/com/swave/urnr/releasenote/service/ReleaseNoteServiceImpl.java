package com.swave.urnr.releasenote.service;

import com.swave.urnr.chatgpt.service.ChatGPTService;
import com.swave.urnr.project.domain.Project;
import com.swave.urnr.project.repository.ProjectRepository;
import com.swave.urnr.releasenote.domain.*;
import com.swave.urnr.releasenote.repository.*;
import com.swave.urnr.releasenote.requestdto.*;
import com.swave.urnr.releasenote.responsedto.*;
import com.swave.urnr.user.domain.User;
import com.swave.urnr.user.domain.UserInProject;
import com.swave.urnr.user.repository.UserInProjectRepository;
import com.swave.urnr.user.repository.UserRepository;
import com.swave.urnr.util.http.HttpResponse;
import com.swave.urnr.util.type.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@EnableTransactionManagement
public class ReleaseNoteServiceImpl implements ReleaseNoteService{
    private final ReleaseNoteRepository releaseNoteRepository;


    private final NoteBlockService noteBlockService;
    private final BlockContextService blockContextService;
    private final ChatGPTService chatGPTService;
    private final SeenCheckService seenCheckService;
    private final CommentService commentService;


    //todo 나중에 다른 쪽에서 구현이 끝나면 service로 갈아 끼울것
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final UserInProjectRepository userInProjectRepository;


    @Override
    @Transactional
    public HttpResponse createReleaseNote(HttpServletRequest request, Long projectId, ReleaseNoteCreateRequestDTO releaseNoteCreateRequestDTO) {
        Date currentDate = new Date();

        User user = userRepository.findById((Long) request.getAttribute("id"))
                .orElseThrow(NoSuchElementException::new);

        Project project = projectRepository.findById(projectId)
                .orElseThrow(NoSuchElementException::new);

        ReleaseNote releaseNote = ReleaseNote.builder()
                .version(releaseNoteCreateRequestDTO.getVersion())
                .lastModifiedDate(currentDate)
                .releaseDate(releaseNoteCreateRequestDTO.getReleaseDate())
                .count(0)
                .isUpdated(false)
                .summary("Temp data until ChatGPT is OKAY")
                .project(project)
                .user(user)
                .commentList(new ArrayList<Comment>())
                .build();

        releaseNoteRepository.save(releaseNote);


        StringBuilder content = new StringBuilder(new String());
        List<NoteBlock> noteBlockList = new ArrayList<>();

        for(NoteBlockCreateRequestDTO noteBlockCreateRequestDTO : releaseNoteCreateRequestDTO.getBlocks()){
            NoteBlock noteBlock = noteBlockService.createNoteBlock(noteBlockCreateRequestDTO, releaseNote);

            List<BlockContext> blockContextList = new ArrayList<>();
            for (BlockContextCreateRequestDTO blockContextCreateRequestDTO : noteBlockCreateRequestDTO.getContexts()){
                BlockContext blockContext = blockContextService.createBlockContext(blockContextCreateRequestDTO, noteBlock);

                content.append(blockContext.getContext());
                blockContextList.add(blockContext);
            }

            noteBlock.setBlockContextList(blockContextList);
            noteBlockList.add(noteBlock);
        }

//        ChatGPTResultDTO ChatGPTResultDTO =  chatGPTService.chatGptResult(
//                new ChatGPTQuestionRequestDTO(content.toString() + "의 내용을 세줄로 요약해줘"));

        releaseNote.setNoteBlockList(noteBlockList);
        //releaseNote.setSummary(ChatGPTResultDTO.getText());

        releaseNoteRepository.flush();

        return HttpResponse.builder()
                .message("Release Note Created")
                .description("Release Note ID : " + releaseNote.getId() + " Created")
                .build();
    }

    //project id를 받아서 해당 project에 연결된 모든 releaseNote를 리스트로 반환 -> 전체 출력용
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
    public ReleaseNoteContentResponseDTO loadReleaseNote(HttpServletRequest request, Long releaseNoteId){
        UserInProject userInProject = releaseNoteRepository.findUserInProjectByUserIdAndReleaseNoteId((Long) request.getAttribute("id"), releaseNoteId);

        if(userInProject == null){
            throw new AccessDeniedException("해당 프로젝트의 접근 권한이 없습니다.");
        }

        ReleaseNote releaseNote = releaseNoteRepository.findById(releaseNoteId)
                .orElseThrow(NoSuchElementException::new);

        ArrayList<CommentContentResponseDTO> commentContentList = commentService.loadCommentList(releaseNoteId);

        ReleaseNoteContentResponseDTO releaseNoteContentResponseDTO = releaseNote.makeReleaseNoteContentDTO();
        releaseNoteContentResponseDTO.setComment(commentContentList);

        releaseNote.addViewCount();
        releaseNoteRepository.flush();

        seenCheckService.createSeenCheck((String) request.getAttribute("username"), releaseNote, userInProject);

        return releaseNoteContentResponseDTO;
    }

    @Override
    @Transactional
    public HttpResponse updateReleaseNote(HttpServletRequest request, Long releaseNoteId, ReleaseNoteUpdateRequestDTO releaseNoteUpdateRequestDTO){
        User user = userRepository.findById((Long) request.getAttribute("id"))
                .orElseThrow(NoSuchElementException::new);

        ReleaseNote releaseNote = releaseNoteRepository.findById(releaseNoteId)
                .orElseThrow(NoSuchElementException::new);

        for(NoteBlock noteBlock : releaseNote.getNoteBlockList()){
            noteBlockService.deleteNoteBlock(noteBlock.getId());
        }

        releaseNote.getNoteBlockList().clear();

        StringBuilder content = new StringBuilder(new String());
        List<NoteBlock> noteBlockList = new ArrayList<>();

        for(NoteBlockUpdateRequestDTO noteBlockUpdateRequestDTO : releaseNoteUpdateRequestDTO.getBlocks()){
            NoteBlock noteBlock = noteBlockService.updateNoteBlock(noteBlockUpdateRequestDTO, releaseNote);

            List<BlockContext> blockContextList = new ArrayList<>();

            for (BlockContextUpdateRequestDTO blockContextUpdateRequestDTO : noteBlockUpdateRequestDTO.getContexts()){
                BlockContext blockContext = blockContextService.updateBlockContext(blockContextUpdateRequestDTO, noteBlock);

                content.append(blockContext.getContext());
                blockContextList.add(blockContext);
            }

            noteBlock.setBlockContextList(blockContextList);
            noteBlockList.add(noteBlock);
        }

//        ChatGPTResultDTO ChatGPTResultDTO =  chatGPTService.chatGptResult(
//                new ChatGPTQuestionRequestDTO(content.toString() + "의 내용을 세줄로 요약해줘"));

        releaseNote.setVersion(releaseNoteUpdateRequestDTO.getVersion());
        releaseNote.setReleaseDate(releaseNoteUpdateRequestDTO.getReleaseDate());
        releaseNote.setLastModifiedDate(new Date());
        releaseNote.getNoteBlockList().addAll(noteBlockList);
        releaseNote.setSummary("Temp data until ChatGPT is OKAY");
        //releaseNote.setSummary(ChatGPTResultDTO.getText());
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
            ReleaseNoteVersionListResponseDTO releaseNoteVersionListResponseDTO = userInProject.makeReleaseNoteVersionListResponseDTO();

            List<ReleaseNote> releaseNoteList = userInProject.getProject().getReleaseNoteList();
            ArrayList<ReleaseNoteVersionResponseDTO> releaseNoteVersionListDTOListResponse = new ArrayList<>();
            for(ReleaseNote releaseNote : releaseNoteList){
                releaseNoteVersionListDTOListResponse.add(releaseNote.makeReleaseNoteVersionResponseDTO());
            }
            releaseNoteVersionListResponseDTO.setReleaseNoteVersionList(releaseNoteVersionListDTOListResponse);

            responseVersionListDTOList.add(releaseNoteVersionListResponseDTO);
        }
        return responseVersionListDTOList;
    };

    @Override
    @Transactional
    public HttpResponse deleteReleaseNote(Long releaseNoteId){
        releaseNoteRepository.deleteById(releaseNoteId);

        return HttpResponse.builder()
                .message("Release Note Deleted")
                .description("Release Note ID : " + releaseNoteId + " Deleted")
                .build();
    }

    @Override
    public ReleaseNoteContentResponseDTO loadRecentReleaseNote(HttpServletRequest request){
        return releaseNoteRepository.findMostRecentReleaseNote((Long) request.getAttribute("id")).makeReleaseNoteContentDTO();
    }
}