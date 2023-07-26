package com.swave.urnr.releasenote.service;

import com.swave.urnr.releasenote.domain.NoteBlock;
import com.swave.urnr.releasenote.domain.ReleaseNote;
import com.swave.urnr.releasenote.requestdto.NoteBlockCreateRequestDTO;
import com.swave.urnr.releasenote.requestdto.NoteBlockUpdateRequestDTO;
import com.swave.urnr.releasenote.responsedto.ReleaseNoteLabelContentResponseDTO;
import com.swave.urnr.releasenote.responsedto.ReleaseNoteLabelCountResponseDTO;

import java.util.List;

public interface NoteBlockService {
    public NoteBlock createNoteBlock(NoteBlockCreateRequestDTO noteBlockCreateRequestDTO, ReleaseNote releaseNote);
    public NoteBlock updateNoteBlock(NoteBlockUpdateRequestDTO noteBlockUpdateRequestDTO, ReleaseNote releaseNote);
    public void deleteNoteBlock(Long noteBlockId);
    public List<ReleaseNoteLabelCountResponseDTO> countLabel(Long projectId);
    public List<ReleaseNoteLabelContentResponseDTO> filterByLabel(Long projectId);
}
