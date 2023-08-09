package com.swave.urnr.releasenote.repository;

import com.swave.urnr.releasenote.domain.Liked;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikedRepository extends JpaRepository<Liked, Long>, LikedCustomRepository {
    Liked findBySeenCheck_Id(Long Id);
}
