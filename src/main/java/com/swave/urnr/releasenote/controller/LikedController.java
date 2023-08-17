package com.swave.urnr.releasenote.controller;

import com.swave.urnr.releasenote.responsedto.LikedCountResponseDTO;
import com.swave.urnr.releasenote.service.LikedService;
import com.swave.urnr.util.http.HttpResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Api(tags = "LikedController")
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://266e8974276247f4b3cad8498606fafb.kakaoiedge.com:80", allowedHeaders = "*", allowCredentials = "true")
public class LikedController {

    private final LikedService likedService;

    @Operation(summary = "좋아요 하나 생성", description = "releaseNoteId의 릴리즈 노트에 내 이름으로 좋아요를 하나 생성합니다. 중복은 불가능 합니다. 유저 정보는 JWT로부터 가져옵니다.")
    @PostMapping("/api/project/release-note/{releaseNoteId}/liked")
    public HttpResponse createLiked(HttpServletRequest request,@PathVariable Long releaseNoteId){
        return likedService.createLiked(request, releaseNoteId);
    }

    @Operation(summary = "좋아요 하나 취소", description = "releaseNoteId의 릴리즈 노트에 내 이름으로된 좋아요를 하나 취소합니다. 유저 정보는 JWT로부터 가져옵니다.")
    @PutMapping("/api/project/release-note/{releaseNoteId}/liked")
    public HttpResponse cancelLiked(HttpServletRequest request,@PathVariable Long releaseNoteId){
        return likedService.cancelLiked(request, releaseNoteId);
    }


    @Cacheable(value="countLike")
    @Operation(summary = "좋아요 갯수 세기", description = "releaseNoteId의 릴리즈 노트에 달린 좋아요의 갯수를 반환합니다.")
    @GetMapping("/api/project/release-note/{releaseNoteId}/liked")
    public LikedCountResponseDTO countLiked(@PathVariable Long releaseNoteId){
        return likedService.countLiked(releaseNoteId);
    }
}
