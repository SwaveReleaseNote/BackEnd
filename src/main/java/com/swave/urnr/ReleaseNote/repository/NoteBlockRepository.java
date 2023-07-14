package com.swave.urnr.ReleaseNote.repository;

import com.swave.urnr.ReleaseNote.domain.NoteBlock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteBlockRepository extends JpaRepository<NoteBlock, Long> {
    List<NoteBlock> findByReleaseNote_Id(Long Id);
}
