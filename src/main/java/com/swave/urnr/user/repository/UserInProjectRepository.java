package com.swave.urnr.user.repository;

import com.swave.urnr.user.domain.UserInProject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserInProjectRepository extends JpaRepository<UserInProject, Long>,UserInProjectCustomRepository {

    List<UserInProject> findByUser_Id(Long Id);
    List<UserInProject> findByProject_Id(Long Id);
}
