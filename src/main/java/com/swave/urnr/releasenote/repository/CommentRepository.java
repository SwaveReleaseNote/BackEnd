package com.swave.urnr.releasenote.repository;

import com.swave.urnr.releasenote.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface CommentRepository extends JpaRepository<Comment, Long> , CommentCustomRepository {
    ArrayList<Comment> findByReleaseNote_Id(Long Id);
}
