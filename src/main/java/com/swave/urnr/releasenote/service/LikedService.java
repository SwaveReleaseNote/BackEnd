package com.swave.urnr.releasenote.service;

import com.swave.urnr.releasenote.responsedto.LikedCountResponseDTO;
import com.swave.urnr.util.http.HttpResponse;

import javax.servlet.http.HttpServletRequest;

public interface LikedService {
    public HttpResponse createLiked(HttpServletRequest request, Long releaseNoteId);

    public HttpResponse cancelLiked(HttpServletRequest request, Long releaseNodeId);

    public LikedCountResponseDTO countLiked(Long releaseNodeId);
} 
