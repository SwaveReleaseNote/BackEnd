package com.swave.urnr.ReleaseNote.controller;

import com.swave.urnr.ReleaseNote.RequestDTO.NewCommentDTO;
import com.swave.urnr.ReleaseNote.RequestDTO.NewReleaseNoteDTO;
import com.swave.urnr.ReleaseNote.RequestDTO.UpdateReleaseNoteDTO;
import com.swave.urnr.ReleaseNote.responseDTO.CommentContentListDTO;
import com.swave.urnr.ReleaseNote.responseDTO.ReleaseNoteContentDTO;
import com.swave.urnr.ReleaseNote.responseDTO.ReleaseNoteContentListDTO;
import com.swave.urnr.ReleaseNote.responseDTO.ReleaseNoteVersionListDTO;
import com.swave.urnr.ReleaseNote.service.ReleaseNoteAllServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", allowCredentials = "true")
public class ReleaseNoteController {
    private final ReleaseNoteAllServiceImpl releaseNoteAllService;

    @PostMapping("/api/project/{projectId}/release/create")
    public Long createReleaseNote(@PathVariable Long projectId, @RequestBody NewReleaseNoteDTO newReleaseNoteDTO) {
        return releaseNoteAllService.createReleaseNote(projectId, newReleaseNoteDTO);
    }

    @PostMapping("/api/project/release/update")
    public Long UpdateReleaseNote(@RequestBody UpdateReleaseNoteDTO updateReleaseNoteDTO) {
        return releaseNoteAllService.updateReleaseNote(updateReleaseNoteDTO);
    }

    @GetMapping("/api/project/release/load/{releaseNoteId}")
    public ReleaseNoteContentDTO loadReleaseNote(@PathVariable Long releaseNoteId) {
        return releaseNoteAllService.loadReleaseNote(releaseNoteId);
    }

    @GetMapping("/api/project/{projectId}/release/load_all")
    public ArrayList<ReleaseNoteContentListDTO> loadReleaseNoteList(@PathVariable Long projectId) {
        return releaseNoteAllService.loadReleaseNoteList(projectId);
    }

    @GetMapping("/api/project/{projectId}/release/version_list")
    public ReleaseNoteVersionListDTO loadVersionList(@PathVariable Long projectId) {
        return releaseNoteAllService.loadVersionList(projectId);
    }

    @PostMapping("/api/project/release/comment/create")
    public Long createComment(@RequestBody NewCommentDTO newCommentDTO){
        return releaseNoteAllService.createComment(newCommentDTO);
    }

    @GetMapping("/api/project/{projectId}/release/comment/load_recent")
    public CommentContentListDTO loadRecentComment(@PathVariable Long projectId){
        return releaseNoteAllService.loadRecentComment(projectId);
    }
}
