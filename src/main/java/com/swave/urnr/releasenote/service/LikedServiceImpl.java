package com.swave.urnr.releasenote.service;

import com.swave.urnr.releasenote.domain.Liked;
import com.swave.urnr.releasenote.domain.SeenCheck;
import com.swave.urnr.releasenote.repository.LikedRepository;
import com.swave.urnr.releasenote.repository.ReleaseNoteRepository;
import com.swave.urnr.releasenote.repository.SeenCheckRepository;
import com.swave.urnr.releasenote.responsedto.LikedCountResponseDTO;
import com.swave.urnr.user.domain.UserInProject;
import com.swave.urnr.util.http.HttpResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
@EnableTransactionManagement
public class LikedServiceImpl implements LikedService {

    private final ReleaseNoteRepository releaseNoteRepository;
    private final SeenCheckRepository seenCheckRepository;
    private final LikedRepository likedRepository;

    @Override
    @Transactional
    public HttpResponse createLiked(HttpServletRequest request, Long releaseNoteId){
        UserInProject userInProject = releaseNoteRepository.findUserInProjectByUserIdAndReleaseNoteId((Long) request.getAttribute("id"), releaseNoteId);
        SeenCheck seenCheck;

        if(userInProject == null){
            throw new AccessDeniedException("해당 프로젝트의 접근 권한이 없습니다.");
        }else{
            seenCheck = seenCheckRepository.findByUserInProjectIdAndReleaseNoteId(userInProject.getId(), releaseNoteId);
        }

        if(null != likedRepository.findBySeenCheck_Id(seenCheck.getId())){
            throw new RuntimeException("이미 좋아요 한 기록이 있습니다.");
        }

        Liked liked = Liked.builder()
                .isLiked(true)
                .seenCheck(seenCheck)
                .build();

        likedRepository.saveAndFlush(liked);

        return HttpResponse.builder()
                .message("Liked Created")
                .description("Liked ID : " + liked.getId() + " Created")
                .build();
    }

    @Override
    @Transactional
    public HttpResponse cancelLiked(HttpServletRequest request, Long releaseNoteId){
        UserInProject userInProject = releaseNoteRepository.findUserInProjectByUserIdAndReleaseNoteId((Long) request.getAttribute("id"), releaseNoteId);
        SeenCheck seenCheck;

        if(userInProject == null){
            throw new AccessDeniedException("해당 프로젝트의 접근 권한이 없습니다.");
        }else{
            seenCheck = seenCheckRepository.findById(userInProject.getId())
                    .orElseThrow(NoSuchElementException::new);
        }

        Liked liked = likedRepository.findBySeenCheck_Id(seenCheck.getId());
        if(null == liked){
            throw new RuntimeException("좋아요 한 기록이 없습니다.");
        }

        liked.setIsLiked(false);

        likedRepository.flush();

        return HttpResponse.builder()
                .message("Liked Cancelled")
                .description("Liked ID : " + liked.getId() + " Cancelled")
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public LikedCountResponseDTO countLiked(Long releaseNoteId){
        LikedCountResponseDTO likedCountResponseDTO = new LikedCountResponseDTO();
        likedCountResponseDTO.setLikedCount(likedRepository.countByLiked(releaseNoteId));
        return likedCountResponseDTO;
    }
}
