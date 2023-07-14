package com.swave.urnr.ReleaseNote.service;

import com.swave.urnr.ReleaseNote.RequestDTO.NewCommentDTO;
import com.swave.urnr.ReleaseNote.responseDTO.CommentContentListDTO;

public interface CommentService {

    public Long createComment(NewCommentDTO newCommentDTO);
    public CommentContentListDTO loadRecentComment(Long projectId);
}
