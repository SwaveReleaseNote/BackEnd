package com.swave.urnr.releasenote.repository;

import com.swave.urnr.releasenote.domain.ReleaseNote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReleaseNoteRepository extends JpaRepository<ReleaseNote, Long>, ReleaseNoteCustomRepository {
    List<ReleaseNote> findByProject_Id(Long Id);
}
