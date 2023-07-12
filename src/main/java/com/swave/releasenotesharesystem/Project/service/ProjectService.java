package com.swave.releasenotesharesystem.Project.service;

import com.swave.releasenotesharesystem.Project.requestDto.ProjectRequestDto;
import com.swave.releasenotesharesystem.Project.responseDto.loadAllProjectResponseDto;
import com.swave.releasenotesharesystem.Project.responseDto.loadOneProjectResponseDto;

import java.util.List;

public interface ProjectService {

    String createProject(ProjectRequestDto projectRequestDto);

    List<loadAllProjectResponseDto> loadProjectList(Long userId);

    loadOneProjectResponseDto loadProject(Long projectId);

    String updateUsers(ProjectRequestDto project);
}
