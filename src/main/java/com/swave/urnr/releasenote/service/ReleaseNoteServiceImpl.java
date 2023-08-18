package com.swave.urnr.releasenote.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swave.urnr.chatgpt.requestdto.ChatGPTQuestionRequestDTO;
import com.swave.urnr.chatgpt.responsedto.ChatGPTResultResponseDTO;
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
import com.swave.urnr.util.kafka.KafkaSSEEmitterSentDTO;
import com.swave.urnr.util.kafka.KafkaService;
import com.swave.urnr.util.kafka.NotificationDTO;
import com.swave.urnr.util.kafka.NotificationEnum;
import com.swave.urnr.util.sse.SSEEmitterService;
import com.swave.urnr.util.sse.SSETypeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
@EnableTransactionManagement
public class ReleaseNoteServiceImpl implements ReleaseNoteService {
    private final ReleaseNoteRepository releaseNoteRepository;

    private final NoteBlockService noteBlockService;
    private final BlockContextService blockContextService;
    //private final ChatGPTService chatGPTService;
    private final SeenCheckService seenCheckService;
    private final CommentService commentService;
    private final LikedService likedService;



    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final UserInProjectRepository userInProjectRepository;

    private final KafkaService kafkaService;


    private ObjectMapper objectMapper = new ObjectMapper();


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

            for (NoteBlockCreateRequestDTO noteBlockCreateRequestDTO : releaseNoteCreateRequestDTO.getBlocks()) {
                NoteBlock noteBlock = noteBlockService.createNoteBlock(noteBlockCreateRequestDTO, releaseNote);

                List<BlockContext> blockContextList = new ArrayList<>();
                for (BlockContextCreateRequestDTO blockContextCreateRequestDTO : noteBlockCreateRequestDTO.getContexts()) {
                    BlockContext blockContext = blockContextService.createBlockContext(blockContextCreateRequestDTO, noteBlock);

                    content.append(blockContext.getContext());
                    blockContextList.add(blockContext);
                }

                noteBlock.setBlockContextList(blockContextList);
                noteBlockList.add(noteBlock);
            }

            //ChatGPTResultResponseDTO ChatGPTResultDTO =  chatGPTService.chatGptResult(
            //    new ChatGPTQuestionRequestDTO(content.toString() + "의 내용을 세줄로 요약해줘"));

            releaseNote.setNoteBlockList(noteBlockList);
            //releaseNote.setSummary(ChatGPTResultDTO.getText());

            releaseNoteRepository.flush();

        /*
        TODO :  Project 모든 유저의 kafka topic에 메세지 발행 / 이후 Listener가 감지하여 해당 유저들 emitter에 알림설정.
        */

        List<UserInProject> projectUserList = project.getUserInProjectList();


        for(UserInProject userInProject : projectUserList){
            Long id = userInProject.getUser().getId();
            /*
            TODO: Releasenote DTO 채우기  / 보내기
             */
            NotificationDTO notificationDTO= new NotificationDTO(NotificationEnum.POST,new Date(System.currentTimeMillis()),releaseNote.getVersion(),projectId);

            // User ID에 message 보내주기
            kafkaService.produceMessage(notificationDTO,String.valueOf(id));
            // Emitter에 UserId 보내주기
            KafkaSSEEmitterSentDTO kafkaSSEEmitterSentDTO = new KafkaSSEEmitterSentDTO(String.valueOf(id),   project.getName()+" 프로젝트에 "+releaseNoteCreateRequestDTO.getVersion() + " 버전이 릴리즈되었습니다.", SSETypeEnum.ALARM);
           try{
               String message = objectMapper.writeValueAsString(kafkaSSEEmitterSentDTO);
               kafkaService.produceMessageAsString(message, "emitter");
           }catch(Exception e){
               e.printStackTrace();
           }

        }




        return HttpResponse.builder()
                .message("Release Note Created")
                .description("Release Note ID : " + releaseNote.getId() + " Created")
                .build();


    }

    //project id를 받아서 해당 project에 연결된 모든 releaseNote를 리스트로 반환 -> 전체 출력용
    @Override
    @Transactional(readOnly = true)
    public ArrayList<ReleaseNoteContentListResponseDTO> loadReleaseNoteList(Long projectId) {
        List<ReleaseNote> releaseNoteList = releaseNoteRepository.findByProject_Id(projectId);

        ArrayList<ReleaseNoteContentListResponseDTO> releaseNoteContentListDTOArrayListResponse = new ArrayList<>();

        for (ReleaseNote releaseNote : releaseNoteList) {
            releaseNoteContentListDTOArrayListResponse.add(releaseNote.makeReleaseNoteContentListDTO());
        }

        return releaseNoteContentListDTOArrayListResponse;
    }

    @Override
    @Transactional(readOnly = true)
    public ReleaseNoteContentResponseDTO loadReleaseNote(HttpServletRequest request, Long releaseNoteId) {

        UserInProject userInProject = releaseNoteRepository.findUserInProjectByUserIdAndReleaseNoteId((Long) request.getAttribute("id"), releaseNoteId);

        if (userInProject == null) {
            throw new AccessDeniedException("해당 프로젝트의 접근 권한이 없습니다.");
        }

        ReleaseNote releaseNote = releaseNoteRepository.findById(releaseNoteId)
                .orElseThrow(NoSuchElementException::new);

        ArrayList<CommentContentResponseDTO> commentContentList = commentService.loadCommentList(releaseNoteId);

        ReleaseNoteContentResponseDTO releaseNoteContentResponseDTO = releaseNote.makeReleaseNoteContentDTO();
        releaseNoteContentResponseDTO.setComment(commentContentList);

        LikedCountResponseDTO likedCountResponseDTO = likedService.countLiked(releaseNoteId);
        releaseNoteContentResponseDTO.setLiked(likedCountResponseDTO.getLikedCount());

        seenCheckService.addViewCount(releaseNoteId);
        seenCheckService.createSeenCheck((String) request.getAttribute("username"), releaseNote, userInProject);

        return releaseNoteContentResponseDTO;
    }

    @Override
    @Transactional
    public HttpResponse updateReleaseNote(HttpServletRequest request, Long releaseNoteId, ReleaseNoteUpdateRequestDTO releaseNoteUpdateRequestDTO) {

            User user = userRepository.findById((Long) request.getAttribute("id"))
                    .orElseThrow(NoSuchElementException::new);

            ReleaseNote releaseNote = releaseNoteRepository.findById(releaseNoteId)
                    .orElseThrow(NoSuchElementException::new);

            for (NoteBlock noteBlock : releaseNote.getNoteBlockList()) {
                noteBlockService.deleteNoteBlock(noteBlock.getId());
            }

            releaseNote.getNoteBlockList().clear();

            StringBuilder content = new StringBuilder(new String());
            List<NoteBlock> noteBlockList = new ArrayList<>();

            for (NoteBlockUpdateRequestDTO noteBlockUpdateRequestDTO : releaseNoteUpdateRequestDTO.getBlocks()) {
                NoteBlock noteBlock = noteBlockService.updateNoteBlock(noteBlockUpdateRequestDTO, releaseNote);

                List<BlockContext> blockContextList = new ArrayList<>();

                for (BlockContextUpdateRequestDTO blockContextUpdateRequestDTO : noteBlockUpdateRequestDTO.getContexts()) {
                    BlockContext blockContext = blockContextService.updateBlockContext(blockContextUpdateRequestDTO, noteBlock);

                    content.append(blockContext.getContext());
                    blockContextList.add(blockContext);
                }

                noteBlock.setBlockContextList(blockContextList);
                noteBlockList.add(noteBlock);
            }

            //ChatGPTResultResponseDTO ChatGPTResultDTO =  chatGPTService.chatGptResult(
            //    new ChatGPTQuestionRequestDTO(content.toString() + "의 내용을 세줄로 요약해줘"));

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
    @Transactional(readOnly = true)
    public ArrayList<ReleaseNoteVersionListResponseDTO> loadProjectVersionList(HttpServletRequest request) {
        ArrayList<ReleaseNoteVersionListResponseDTO> responseVersionListDTOList = new ArrayList<>();
        List<UserInProject> userInProjectList = userInProjectRepository.findByUser_Id((Long) request.getAttribute("id"));

        for (UserInProject userInProject : userInProjectList) {
            ReleaseNoteVersionListResponseDTO releaseNoteVersionListResponseDTO = userInProject.makeReleaseNoteVersionListResponseDTO();

            List<ReleaseNote> releaseNoteList = userInProject.getProject().getReleaseNoteList();
            ArrayList<ReleaseNoteVersionResponseDTO> releaseNoteVersionListDTOListResponse = new ArrayList<>();
            for (ReleaseNote releaseNote : releaseNoteList) {
                releaseNoteVersionListDTOListResponse.add(releaseNote.makeReleaseNoteVersionResponseDTO());
            }
            releaseNoteVersionListResponseDTO.setReleaseNoteVersionList(releaseNoteVersionListDTOListResponse);

            responseVersionListDTOList.add(releaseNoteVersionListResponseDTO);
        }
        return responseVersionListDTOList;
    }



    @Override
    @Transactional
    public HttpResponse deleteReleaseNote(HttpServletRequest request, Long releaseNoteId) {

            releaseNoteRepository.deleteById(releaseNoteId);

            return HttpResponse.builder()
                    .message("Release Note Deleted")
                    .description("Release Note ID : " + releaseNoteId + " Deleted")
                    .build();

    }

    @Override
    @Transactional(readOnly = true)
    public ReleaseNoteContentResponseDTO loadRecentReleaseNote(HttpServletRequest request) {
        ReleaseNote releaseNote = releaseNoteRepository.findMostRecentReleaseNote((Long) request.getAttribute("id"));
        if (releaseNote == null) {
            return null;
        } else {
            return releaseNote.makeReleaseNoteContentDTO();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ReleaseNoteLastestVersionResponeDTO loadReleaseNoteLastestVersion(Long projectId){
        List<ReleaseNote> releaseNoteList = releaseNoteRepository.findByProject_Id(projectId);
        ReleaseNoteLastestVersionResponeDTO releaseNoteLastestVersionResponeDTO = new ReleaseNoteLastestVersionResponeDTO();
        if(releaseNoteList != null){
            releaseNoteLastestVersionResponeDTO.setVersion(releaseNoteList.get(releaseNoteList.size()-1).getVersion());
        }else{
            releaseNoteLastestVersionResponeDTO.setVersion(null);
        }
        return releaseNoteLastestVersionResponeDTO;
    }

}