package com.swave.urnr.releasenote.controller;

import com.swave.urnr.releasenote.requestdto.ReleaseNoteCreateRequestDTO;
import com.swave.urnr.releasenote.requestdto.ReleaseNoteUpdateRequestDTO;
import com.swave.urnr.releasenote.responsedto.*;
import com.swave.urnr.releasenote.service.ReleaseNoteService;
import com.swave.urnr.util.http.HttpResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

import lombok.RequiredArgsConstructor;

@Api(tags = "ReleaseNoteController")
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", allowCredentials = "true")
public class ReleaseNoteController {
    private final ReleaseNoteService releaseNoteService;

    @Operation(summary="릴리즈 노트 생성", description="projectId의 프로젝트에 릴리즈 노트를 생성합니다. 유저 정보는 JWT로부터 가져옵니다.")
    @PostMapping("/api/project/{projectId}/release-note")
    public HttpResponse createReleaseNote(HttpServletRequest request, @PathVariable Long projectId, @RequestBody ReleaseNoteCreateRequestDTO releaseNoteCreateRequestDTO) {
        return releaseNoteService.createReleaseNote(request, projectId, releaseNoteCreateRequestDTO);
    }

    @Operation(summary="릴리즈 노트 수정", description="릴리즈 노트를 수정합니다. 유저 정보는 JWT로부터 가져옵니다.")
    @PatchMapping("/api/project/release-note/{releaseNoteId}")
    public HttpResponse UpdateReleaseNote(HttpServletRequest request, @PathVariable Long releaseNoteId, @RequestBody ReleaseNoteUpdateRequestDTO releaseNoteUpdateRequestDTO) {
        return releaseNoteService.updateReleaseNote(request, releaseNoteId, releaseNoteUpdateRequestDTO);
    }

    @Operation(summary="릴리즈 노트 한개 받기", description="releaseNoteId의 릴리즈 노트를 읽어옵니다. 댓글이 포함됩니다.")
    @GetMapping("/api/project/release-note/{releaseNoteId}")
    public ReleaseNoteContentResponseDTO loadReleaseNote(HttpServletRequest request, @PathVariable Long releaseNoteId) {
        return releaseNoteService.loadReleaseNote(request, releaseNoteId);
    }

    @Operation(summary="릴리즈 노트 리스트 받기", description="projectId의 프로젝트의 모든 릴리즈 노트를 읽어옵니다. (*GPT 요약본으로)")
    @GetMapping("/api/project/{projectId}/release-notes")
    public ArrayList<ReleaseNoteContentListResponseDTO> loadReleaseNoteList(@PathVariable Long projectId) {
        return releaseNoteService.loadReleaseNoteList(projectId);
    }

    @Operation(summary="릴리즈 노트 버전 리스트 받기", description="내가 소속된 모든 프로젝트의 릴리즈 노트 버전정보를 읽어옵니다. 유저 정보는 JWT로부터 가져옵니다.")
    @GetMapping("/api/project/release-note/version-list")
    public ArrayList<ReleaseNoteVersionListResponseDTO> loadProjectVersionList(HttpServletRequest request) {
        return releaseNoteService.loadProjectVersionList(request);
    }

    @Operation(summary="릴리즈 노트 삭제", description="releaseNoteId의 릴리즈노트를 삭제 합니다.")
    @DeleteMapping("/api/project/release-note/{releaseNoteId}")
    public HttpResponse deleteReleaseNote(@PathVariable Long releaseNoteId) {
        return releaseNoteService.deleteReleaseNote(releaseNoteId);
    }

    @Operation(summary = "구독 또는 개발에 참여중인 프로젝트 내에서 가장 최근 릴리즈 노트 하나 받기", description="내가 소속된 모든 프로젝트에서 가장 최근 릴리즈 노트 하나를 읽어옵니다. 유저 정보는 JWT로부터 가져옵니다." )
    @GetMapping("/api/project/release-note/recent-release-note")
    public ReleaseNoteContentResponseDTO loadRecentReleaseNote(HttpServletRequest request){
        return releaseNoteService.loadRecentReleaseNote(request);
    }
}
