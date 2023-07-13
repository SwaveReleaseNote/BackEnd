package com.swave.releasenotesharesystem.ReleaseNote.repository;

import com.swave.releasenotesharesystem.ReleaseNote.domain.ReleaseNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface ReleaseNoteRepository extends JpaRepository<ReleaseNote, Long>,ReleaseNoteCustomRepository {
    List<ReleaseNote> findByProject_Id(Long Id);
}
