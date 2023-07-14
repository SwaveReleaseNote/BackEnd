package com.swave.urnr.project.repository;

import com.swave.urnr.project.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long>,ProjectCustomRepository {
}
