package com.swave.urnr.releasenote.service;

import com.swave.urnr.releasenote.domain.ReleaseNote;
import com.swave.urnr.releasenote.domain.SeenCheck;
import com.swave.urnr.releasenote.repository.ReleaseNoteRepository;
import com.swave.urnr.releasenote.repository.SeenCheckRepository;
import com.swave.urnr.user.domain.UserInProject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
@EnableTransactionManagement
public class SeenCheckServiceImpl implements SeenCheckService {

    private final SeenCheckRepository seenCheckRepository;
    private final ReleaseNoteRepository releaseNoteRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
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

        return seenCheck;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void addViewCount(Long releaseNoteId){
        ReleaseNote releaseNote = releaseNoteRepository.findById(releaseNoteId)
                .orElseThrow(NoSuchElementException::new);
        releaseNote.addViewCount();
        releaseNoteRepository.flush();
    }
}
