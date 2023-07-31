package com.swave.urnr.releasenote.repository;

import com.swave.urnr.releasenote.domain.SeenCheck;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeenCheckRepository extends JpaRepository<SeenCheck, Long> {
    SeenCheck findByUserInProjectIdAndReleaseNoteId(Long userInProjectId, Long releaseNoteId);
}
