package com.swave.releasenotesharesystem.Project.controller;

import com.swave.releasenotesharesystem.Project.requestDto.ProjectCreateRequestDto;
import com.swave.releasenotesharesystem.Project.requestDto.ProjectUpdateRequestDto;
import com.swave.releasenotesharesystem.Project.responseDto.LoadAllProjectResponseDto;
import com.swave.releasenotesharesystem.Project.responseDto.LoadOneProjectResponseDto;
import com.swave.releasenotesharesystem.Project.service.ProjectService;
import lombok.RequiredArgsConstructor;
import com.swave.releasenotesharesystem.Util.http.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.SQLDelete;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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
    //구독자는 어떻게 빼지?
    //최신릴리즈노트 가져오기
    @PostMapping("/create")
    public HttpResponse createProject(HttpServletRequest request, @RequestBody ProjectCreateRequestDto project){;
        return projectService.createProject(request, project);
    }

    /*@PostMapping("/create")
    public ProjectRequestDto createProject(@RequestBody ProjectRequestDto project){
        projectService.createProject(project);
        return project;
    }*/
    //read 전체, 하나, 친구초대

    //read 전체 가져오기 dto로 쇼로록
    //id구분 해줘야해
    //카운트까지 해서 가져오자
    //@GetMapping("/load/all/{userId}")
    //@PathVariable Long userId
    @GetMapping("/load/all")
    public List<LoadAllProjectResponseDto> loadProjectList(HttpServletRequest request){
        return projectService.loadProjectList(request);
    }

    //하나씩 찾아도 유저가 있어야 하지 않은가?
    //사실 이건 릴리즈노트 기능이다
    @GetMapping("/load/one/{projectId}")
    public LoadOneProjectResponseDto loadProject(@PathVariable Long projectId){
        return projectService.loadProject(projectId);
    }

    //멤버 편집

    @PutMapping("/update/{projectId}")
    public ProjectUpdateRequestDto updateProject(@RequestBody ProjectUpdateRequestDto projectUpdateRequestDto){
        return projectService.updateProject(projectUpdateRequestDto);

    }
    //프로젝트 삭제

    //@DeleteMapping("/delete/{projectId}")
    //@SQLDelete()
    //QUERY DSL
}
