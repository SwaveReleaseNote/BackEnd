package com.swave.urnr.releasenote.repository;

import com.swave.urnr.releasenote.responsedto.CommentContentResponseDTO;

import java.util.List;

public interface CommentCustomRepository {
    List<CommentContentResponseDTO> findTop5RecentComment(Long projectId);
}
