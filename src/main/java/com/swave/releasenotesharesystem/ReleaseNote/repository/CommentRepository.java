package com.swave.releasenotesharesystem.ReleaseNote.repository;

import com.swave.releasenotesharesystem.ReleaseNote.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface CommentRepository extends JpaRepository<Comment, Long> , CommentCustomRepository {
    ArrayList<Comment> findByReleaseNote_Id(Long Id);
    ArrayList<Comment> findAllByOrderByLastModifiedDateDesc();
}
