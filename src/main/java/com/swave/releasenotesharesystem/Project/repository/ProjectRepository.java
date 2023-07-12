package com.swave.releasenotesharesystem.Project.repository;

import com.swave.releasenotesharesystem.Project.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface ProjectRepository extends JpaRepository<Project, Long>,ProjectCustomRepository {
}
