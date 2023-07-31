package com.swave.urnr.releasenote.service;

import com.swave.urnr.releasenote.domain.BlockContext;
import com.swave.urnr.releasenote.domain.NoteBlock;
import com.swave.urnr.releasenote.domain.ReleaseNote;
import com.swave.urnr.releasenote.domain.SeenCheck;
import com.swave.urnr.releasenote.repository.BlockContextRepository;
import com.swave.urnr.releasenote.repository.SeenCheckRepository;
import com.swave.urnr.releasenote.requestdto.BlockContextCreateRequestDTO;
import com.swave.urnr.releasenote.requestdto.BlockContextUpdateRequestDTO;
import com.swave.urnr.user.domain.UserInProject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@EnableTransactionManagement
public class SeenCheckServiceImpl implements SeenCheckService {

    private final SeenCheckRepository seenCheckRepository;
    @Override
    @Transactional
    public SeenCheck createSeenCheck(String username , ReleaseNote releaseNote, UserInProject userInProject){
        if(seenCheckRepository.findByUserInProjectIdAndReleaseNoteId(userInProject.getId(), releaseNote.getId()) != null) {
            return null;
        }

        SeenCheck seenCheck = SeenCheck.builder()
                .userName(username)
                .releaseNote(releaseNote)
                .userInProject(userInProject)
                .build();

        seenCheckRepository.saveAndFlush(seenCheck);
        seenCheckRepository.flush();

        return seenCheck;
    }

}
