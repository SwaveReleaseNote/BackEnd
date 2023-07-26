package com.swave.urnr.releasenote.controller;

import com.swave.urnr.releasenote.requestdto.CommentCreateRequestDTO;
import com.swave.urnr.releasenote.responsedto.CommentContentListResponseDTO;
import com.swave.urnr.releasenote.service.CommentService;
import com.swave.urnr.util.http.HttpResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Api(tags = "CommentController")
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", allowCredentials = "true")
public class CommentController {
    private final CommentService commentService;

    @Operation(summary = "댓글 하나 쓰기", description = "releaseNoteId의 릴리즈 노트에 댓글을 생성합니다. 유저 정보는 JWT로부터 가져옵니다.")
    @PostMapping("/api/project/release-note/{releaseNoteId}/comment")
    public HttpResponse createComment(HttpServletRequest request, @PathVariable Long releaseNoteId , @RequestBody CommentCreateRequestDTO commentCreateRequestDTO){
        return commentService.createComment(request, releaseNoteId , commentCreateRequestDTO);
    }

    @Operation(summary="최근 댓글 5개 읽기", description="projectId의 프로젝트에서 최근에 달린 댓글 5개를 보여줍니다.")
    @GetMapping("/api/project/{projectId}/release-note/recent-comments")
    public CommentContentListResponseDTO loadRecentComment(@PathVariable Long projectId){
        return commentService.loadRecentComment(projectId);
    }

    @Operation(summary="댓글 하나 삭제", description="commentId의 댓글을 삭제합니다.")
    @DeleteMapping("/api/project/release-note/comment/{commentId}")
    public HttpResponse deleteComment(@PathVariable Long commentId){
        return commentService.deleteComment(commentId);
    }
}
