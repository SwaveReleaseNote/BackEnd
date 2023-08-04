package com.swave.urnr.project.repository;

import com.swave.urnr.project.domain.Project;
import com.swave.urnr.user.domain.User;
import com.swave.urnr.util.elasticSearch.ElasticsearchRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long>,ProjectCustomRepository, ElasticsearchRepository<Project, Long> {
}
