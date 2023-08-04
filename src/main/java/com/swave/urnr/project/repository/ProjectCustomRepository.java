package com.swave.urnr.project.repository;


import com.swave.urnr.project.domain.Project;
import com.swave.urnr.project.domain.ProjectElasticsearch;
import com.swave.urnr.project.responsedto.ProjectSearchListResponseDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProjectCustomRepository {


    List<ProjectSearchListResponseDTO> searchProjectByName(String keyword);
    List<ProjectSearchListResponseDTO> searchProjectByDescription(String keyword);
    List<ProjectSearchListResponseDTO> searchProjectByManager(String keyword);
    List<ProjectSearchListResponseDTO> searchProjectByDeveloper(String keyword);

    List<ProjectElasticsearch> searchProject(String keyword, Pageable pageable);

}
