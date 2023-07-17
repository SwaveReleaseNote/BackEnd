package com.swave.urnr.project.service;

import com.swave.urnr.project.requestdto.ProjectUpdateRequestDTO;
import com.swave.urnr.project.responsedto.ProjectContentResponseDto;
import com.swave.urnr.project.responsedto.ProjectListResponseDTO;
import com.swave.urnr.project.requestdto.ProjectCreateRequestDTO;

import javax.servlet.http.HttpServletRequest;
import com.swave.urnr.util.http.HttpResponse;
import java.util.List;

public interface ProjectService {

    HttpResponse createProject(HttpServletRequest request, ProjectCreateRequestDTO projectRequestDto);

    List<ProjectListResponseDTO> loadProjectList(HttpServletRequest request);

    ProjectContentResponseDto loadProject(Long projectId);

    ProjectUpdateRequestDTO updateProject(ProjectUpdateRequestDTO projectUpdateRequestDto);

    HttpResponse deleteProject(Long projectId);

    //String updateUsers(ProjectRequestDto project);

}
