package com.swave.urnr.project.service;

import com.swave.urnr.project.exception.NotAuthorizedException;
import com.swave.urnr.project.requestdto.ProjectKeywordRequestContentDTO;
import com.swave.urnr.project.requestdto.ProjectUpdateRequestDTO;
import com.swave.urnr.project.responsedto.*;
import com.swave.urnr.project.requestdto.ProjectCreateRequestDTO;

import javax.servlet.http.HttpServletRequest;

import com.swave.urnr.util.http.HttpResponse;
import com.swave.urnr.util.type.UserRole;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public interface ProjectService {

    HttpResponse createProject(HttpServletRequest request, ProjectCreateRequestDTO projectRequestDto) throws InterruptedException;

    List<ProjectListResponseDTO> loadProjectList(HttpServletRequest request);

    ProjectContentResponseDTO loadProject(HttpServletRequest request, Long projectId) throws NotAuthorizedException;

    ProjectManagementContentResponseDTO loadManagementProject(HttpServletRequest request,Long projectId) throws IOException, NotAuthorizedException;

    ProjectManagementContentResponseDTO loadManagementProjectJPA(HttpServletRequest request, Long projectId) throws NotAuthorizedException;

    ProjectUpdateRequestDTO updateProject(HttpServletRequest request, Long projectId, ProjectUpdateRequestDTO projectUpdateRequestDto) throws NotAuthorizedException;

    HttpResponse deleteProject(HttpServletRequest request,Long projectId) throws NotAuthorizedException;

    ProjectSearchResultListResponseDTO searchProject(ProjectKeywordRequestContentDTO projectKeywordRequestContentDTO) throws UnsupportedEncodingException;

    List<ProjectUserCheckDTO> checkUser(Long projectId);

    UserRole getRole(HttpServletRequest request, Long projectId);


    //String updateUsers(ProjectRequestDto project);

}
