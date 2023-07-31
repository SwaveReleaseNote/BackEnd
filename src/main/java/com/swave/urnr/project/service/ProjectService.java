package com.swave.urnr.project.service;

import com.swave.urnr.project.requestdto.ProjectKeywordRequestContentDTO;
import com.swave.urnr.project.requestdto.ProjectUpdateRequestDTO;
import com.swave.urnr.project.responsedto.*;
import com.swave.urnr.project.requestdto.ProjectCreateRequestDTO;

import javax.servlet.http.HttpServletRequest;

import com.swave.urnr.util.http.HttpResponse;

import java.io.UnsupportedEncodingException;
import java.util.List;

public interface ProjectService {

    HttpResponse createProject(HttpServletRequest request, ProjectCreateRequestDTO projectRequestDto);

    List<ProjectListResponseDTO> loadProjectList(HttpServletRequest request);

    ProjectContentResponseDTO loadProject(Long projectId);

    ProjectManagementContentResponseDTO loadManagementProject(HttpServletRequest request,Long projectId);

    ProjectManagementContentResponseDTO loadManagementProjectJPA(HttpServletRequest request, Long projectId);

    ProjectUpdateRequestDTO updateProject(Long projectId, ProjectUpdateRequestDTO projectUpdateRequestDto);

    HttpResponse deleteProject(Long projectId);

    ProjectSearchResultListResponseDTO searchProject(ProjectKeywordRequestContentDTO projectKeywordRequestContentDTO) throws UnsupportedEncodingException;

    List<ProjectUserCheckDTO> checkUser(Long projectId);


    //String updateUsers(ProjectRequestDto project);

}
