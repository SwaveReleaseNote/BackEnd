package com.swave.urnr.releasenote.service;

import com.swave.urnr.releasenote.domain.ReleaseNote;
import com.swave.urnr.releasenote.requestdto.ReleaseNoteCreateRequestDTO;
import com.swave.urnr.releasenote.requestdto.ReleaseNoteUpdateRequestDTO;
import com.swave.urnr.releasenote.responsedto.ReleaseNoteContentResponseDTO;
import com.swave.urnr.releasenote.responsedto.ReleaseNoteContentListResponseDTO;
import com.swave.urnr.releasenote.responsedto.ReleaseNoteLabelCountResponseDTO;
import com.swave.urnr.releasenote.responsedto.ReleaseNoteVersionListResponseDTO;
import com.swave.urnr.util.http.HttpResponse;
import com.swave.urnr.user.domain.UserInProject;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public interface ReleaseNoteService {
    public HttpResponse createReleaseNote(HttpServletRequest request, Long projectId, ReleaseNoteCreateRequestDTO releaseNoteCreateRequestDTO);
    public HttpResponse updateReleaseNote(HttpServletRequest request, Long releaseNoteId, ReleaseNoteUpdateRequestDTO releaseNoteUpdateRequestDTO);
    public HttpResponse deleteReleaseNote(Long releaseNoteId);
    public ArrayList<ReleaseNoteContentListResponseDTO> loadReleaseNoteList(Long projectId);
    public ReleaseNoteContentResponseDTO loadReleaseNote(HttpServletRequest request, Long releaseNoteId);
    public ArrayList<ReleaseNoteVersionListResponseDTO> loadProjectVersionList(HttpServletRequest request);
    public ReleaseNoteContentResponseDTO loadRecentReleaseNote(HttpServletRequest request);

}
