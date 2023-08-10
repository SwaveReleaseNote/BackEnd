package com.swave.urnr.releasenote.repository;

public interface LikedCustomRepository {
    long countByLiked(Long userInProjectId);
}
