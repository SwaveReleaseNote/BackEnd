package com.swave.urnr.releasenote.controller;

import com.swave.urnr.releasenote.requestdto.ReleaseNoteCreateRequestDTO;
import com.swave.urnr.releasenote.requestdto.ReleaseNoteUpdateRequestDTO;
import com.swave.urnr.releasenote.responsedto.*;
import com.swave.urnr.releasenote.service.ReleaseNoteServiceImpl;
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
    private final ReleaseNoteServiceImpl releaseNoteAllService;

    @Operation(summary="릴리즈 노트 생성", description="projectId의 프로젝트에 릴리즈 노트를 생성합니다. 유저 정보는 JWT로부터 가져옵니다.")
    @PostMapping("/api/project/{projectId}/release/create")
    public HttpResponse createReleaseNote(HttpServletRequest request, @PathVariable Long projectId, @RequestBody ReleaseNoteCreateRequestDTO releaseNoteCreateRequestDTO) {
        return releaseNoteAllService.createReleaseNote(request, projectId, releaseNoteCreateRequestDTO);
    }

    @Operation(summary="릴리즈 노트 수정", description="릴리즈 노트를 수정합니다. 유저 정보는 JWT로부터 가져옵니다.")
    @PostMapping("/api/project/release/update")
    public HttpResponse UpdateReleaseNote(HttpServletRequest request, @RequestBody ReleaseNoteUpdateRequestDTO releaseNoteUpdateRequestDTO) {
        return releaseNoteAllService.updateReleaseNote(request, releaseNoteUpdateRequestDTO);
    }

    @Operation(summary="릴리즈 노트 한개 받기", description="releaseNoteId의 릴리즈 노트를 읽어옵니다. 댓글이 포함됩니다.")
    @GetMapping("/api/project/release/load/{releaseNoteId}")
    public ReleaseNoteContentResponseDTO loadReleaseNote(@PathVariable Long releaseNoteId) {
        return releaseNoteAllService.loadReleaseNote(releaseNoteId);
    }

    @Operation(summary="릴리즈 노트 리스트 받기", description="projectId의 프로젝트의 모든 릴리즈 노트를 읽어옵니다. (*GPT 요약본으로)")
    @GetMapping("/api/project/{projectId}/release/load_all")
    public ArrayList<ReleaseNoteContentListResponseDTO> loadReleaseNoteList(@PathVariable Long projectId) {
        return releaseNoteAllService.loadReleaseNoteList(projectId);
    }

    @Operation(summary="릴리즈 노트 버전 리스트 받기", description="내가 소속된 모든 프로젝트의 릴리즈 노트 버전정보를 읽어옵니다. 유저 정보는 JWT로부터 가져옵니다.")
    @GetMapping("/api/project/release/version_list")
    public ArrayList<ReleaseNoteVersionListResponseDTO> loadProjectVersionList(HttpServletRequest request) {
        return releaseNoteAllService.loadProjectVersionList(request);
    }

    @Operation(summary="릴리즈 노트 버전 리스트 받기", description="내가 소속된 모든 프로젝트의 릴리즈 노트 버전정보를 읽어옵니다. 유저 정보는 JWT로부터 가져옵니다.")
    @DeleteMapping("/api/project/release/delete/{releaseNoteId}")
    public HttpResponse deleteReleaseNote(@PathVariable Long releaseNoteId) {
        return releaseNoteAllService.deleteReleaseNote(releaseNoteId);
    }


}
