package com.swave.urnr.ReleaseNote.repository;

import com.swave.urnr.ReleaseNote.domain.ReleaseNote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReleaseNoteRepository extends JpaRepository<ReleaseNote, Long>,ReleaseNoteCustomRepository {
    List<ReleaseNote> findByProject_Id(Long Id);
}
