package com.swave.urnr.project.repository;


import com.swave.urnr.project.responsedto.ProjectSearchListResponseDTO;

import java.util.List;

public interface ProjectCustomRepository {


    List<ProjectSearchListResponseDTO> searchProjectByName(String keyword);
    List<ProjectSearchListResponseDTO> searchProjectByDescription(String keyword);
    List<ProjectSearchListResponseDTO> searchProjectByManager(String keyword);
    List<ProjectSearchListResponseDTO> searchProjectByDeveloper(String keyword);

}
