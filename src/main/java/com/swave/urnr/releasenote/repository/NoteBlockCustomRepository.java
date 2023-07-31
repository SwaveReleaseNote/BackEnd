package com.swave.urnr.releasenote.repository;

import com.swave.urnr.releasenote.responsedto.ReleaseNoteLabelContentResponseDTO;
import com.swave.urnr.releasenote.responsedto.ReleaseNoteLabelCountResponseDTO;

import java.util.List;

public interface NoteBlockCustomRepository {
    List<ReleaseNoteLabelCountResponseDTO> countByLabel(Long projectId);
    List<ReleaseNoteLabelContentResponseDTO> filterByLabel(Long projectId);
}
