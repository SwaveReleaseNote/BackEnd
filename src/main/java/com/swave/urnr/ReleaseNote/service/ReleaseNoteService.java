package com.swave.urnr.ReleaseNote.service;

import com.swave.urnr.ReleaseNote.RequestDTO.NewReleaseNoteDTO;
import com.swave.urnr.ReleaseNote.RequestDTO.UpdateReleaseNoteDTO;
import com.swave.urnr.ReleaseNote.responseDTO.ReleaseNoteContentDTO;
import com.swave.urnr.ReleaseNote.responseDTO.ReleaseNoteContentListDTO;
import com.swave.urnr.ReleaseNote.responseDTO.ReleaseNoteVersionListDTO;

import java.util.ArrayList;

public interface  ReleaseNoteService {

    public Long createReleaseNote(Long projectId, NewReleaseNoteDTO newReleaseNoteDTO);
    public Long updateReleaseNote(UpdateReleaseNoteDTO updateReleaseNoteDTO);
    public ArrayList<ReleaseNoteContentListDTO> loadReleaseNoteList(Long projectId);
    public ReleaseNoteContentDTO loadReleaseNote(Long releaseNoteId);
    public ReleaseNoteVersionListDTO loadVersionList(Long projectId);
}
