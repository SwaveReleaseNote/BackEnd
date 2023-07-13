package com.swave.releasenotesharesystem.Project.service;

import com.swave.releasenotesharesystem.Project.requestDto.ProjectCreateRequestDto;
import com.swave.releasenotesharesystem.Project.requestDto.ProjectUpdateRequestDto;
import com.swave.releasenotesharesystem.Project.responseDto.LoadAllProjectResponseDto;
import com.swave.releasenotesharesystem.Project.responseDto.LoadOneProjectResponseDto;

import javax.servlet.http.HttpServletRequest;
import com.swave.releasenotesharesystem.Util.http.HttpResponse;
import java.util.List;

public interface ProjectService {

    HttpResponse createProject(HttpServletRequest request, ProjectCreateRequestDto projectRequestDto);

    List<LoadAllProjectResponseDto> loadProjectList(HttpServletRequest request);

    LoadOneProjectResponseDto loadProject(Long projectId);

    ProjectUpdateRequestDto updateProject(ProjectUpdateRequestDto projectUpdateRequestDto);

    //String updateUsers(ProjectRequestDto project);

}
