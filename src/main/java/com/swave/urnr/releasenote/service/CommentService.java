package com.swave.urnr.releasenote.service;

import com.swave.urnr.releasenote.requestdto.CommentCreateRequestDTO;
import com.swave.urnr.releasenote.responsedto.CommentContentListResponseDTO;
import com.swave.urnr.util.http.HttpResponse;

import javax.servlet.http.HttpServletRequest;

public interface CommentService {

    public HttpResponse createComment(HttpServletRequest request, Long releaseNoteId , CommentCreateRequestDTO commentCreateRequestDTO);
    public CommentContentListResponseDTO loadRecentComment(Long projectId);
    public HttpResponse deleteComment(Long commentId);
}
