package com.swave.releasenotesharesystem.Project.controller;

import com.swave.releasenotesharesystem.Project.requestDto.ProjectRequestDto;
import com.swave.releasenotesharesystem.Project.responseDto.loadAllProjectResponseDto;
import com.swave.releasenotesharesystem.Project.responseDto.loadOneProjectResponseDto;
import com.swave.releasenotesharesystem.Project.service.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/project")
@CrossOrigin

@Slf4j
@RequiredArgsConstructor
public class ProjectController {
/*
    @Autowired
    private ProjectService projectService;*/

    private final ProjectService projectService;

    //0706 : create 생성
    //프로젝트를 생성할 때 생성자가 userinproject에 들어가야해
    @PostMapping("/create")
    public String createProject(@RequestBody ProjectRequestDto project){
        return projectService.createProject(project);
    }

    //read 전체, 하나, 친구초대

    //read 전체 가져오기 dto로 쇼로록
    //id구분 해줘야해
    @GetMapping("/load/all/{userId}")
    public List<loadAllProjectResponseDto> loadProjectList(@PathVariable Long userId){
        return projectService.loadProjectList(userId);
    }

    //하나씩 찾아도 유저가 있어야 하지 않은가?
    @GetMapping("/load/one/{projectId}")
    public loadOneProjectResponseDto loadProject(@PathVariable Long projectId){
        return projectService.loadProject(projectId);
    }
    
    
    //팀 인원수 총합 구독자 빼고
    //멤버 편집
    //프로젝트 삭제
}
