package com.swave.urnr.user.repository;

import com.swave.urnr.user.domain.UserInProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserInProjectRepository extends JpaRepository<UserInProject, Long>,UserInProjectCustomRepository {

    List<UserInProject> findByUser_Id(Long Id);
    List<UserInProject> findByProject_Id(Long Id);
}
