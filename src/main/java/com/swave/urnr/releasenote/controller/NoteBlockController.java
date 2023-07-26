package com.swave.urnr.releasenote.controller;

import com.swave.urnr.releasenote.requestdto.CommentCreateRequestDTO;
import com.swave.urnr.releasenote.responsedto.CommentContentListResponseDTO;
import com.swave.urnr.releasenote.responsedto.ReleaseNoteLabelContentResponseDTO;
import com.swave.urnr.releasenote.responsedto.ReleaseNoteLabelCountResponseDTO;
import com.swave.urnr.releasenote.service.CommentService;
import com.swave.urnr.releasenote.service.NoteBlockService;
import com.swave.urnr.util.http.HttpResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(tags = "NoteBlockController")
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", allowCredentials = "true")
public class NoteBlockController {
    private final NoteBlockService noteBlockService;

    @Operation(summary = "프로젝트 전체 릴리즈 노트 라벨로 카운트 하기", description = "projectId의 프로젝트에 포함된 모든 릴리즈 노트의 라벨개수를 카운트 해서 돌려줍니다.")
    @GetMapping("/api/project/{projectId}/release-note/label/count")
    public List<ReleaseNoteLabelCountResponseDTO> countLabel(@PathVariable Long projectId){
        return noteBlockService.countLabel(projectId);
    }

    @Operation(summary = "프로젝트 전체 릴리즈 노트 라벨로 필터링 하기", description = "projectId의 프로젝트에 포함된 모든 릴리즈 노트를 라벨로 필터링 해서 보여줍니다.")
    @GetMapping("/api/project/{projectId}/release-note/label/filter")
    public List<ReleaseNoteLabelContentResponseDTO> filterByLabel(@PathVariable Long projectId){
        return noteBlockService.filterByLabel(projectId);
    }

}
