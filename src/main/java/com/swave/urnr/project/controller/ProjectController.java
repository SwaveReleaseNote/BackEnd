package com.swave.urnr.project.controller;

import com.swave.urnr.project.requestdto.ProjectCreateRequestDTO;
import com.swave.urnr.project.requestdto.ProjectUpdateRequestDTO;
import com.swave.urnr.project.responsedto.ProjectContentResponseDTO;
import com.swave.urnr.project.responsedto.ProjectListResponseDTO;

import com.swave.urnr.project.responsedto.ProjectManagementContentResponseDTO;
import com.swave.urnr.project.responsedto.ProjectUserCheckDTO;
import com.swave.urnr.project.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import com.swave.urnr.util.http.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import java.util.List;

@RestController
@RequestMapping("/api/project")
@CrossOrigin
@Slf4j
@RequiredArgsConstructor
public class ProjectController {


    private final ProjectService projectService;

    //0706 : create 생성

    @Operation(summary="프로젝트 생성", description="JWT에서 유저정보를 받아 프로젝트를 생성합니다.")
    @PostMapping("/create")
    public HttpResponse createProject(HttpServletRequest request, @RequestBody ProjectCreateRequestDTO project){;
        return projectService.createProject(request, project);
    }

    //read 전체, 하나, 친구초대

    //read 전체 가져오기 dto로 쇼로록
    //id구분 해줘야해
    //카운트까지 해서 가져오자
    //@GetMapping("/load/all/{userId}")
    //@PathVariable Long userId
    @Operation(summary="프로젝트 전체 가져오기", description="JWT에서 유저정보를 받아 해당 유저의 프로젝트 전체를 가져옵니다.")
    @GetMapping("/load/all")
    public List<ProjectListResponseDTO> loadProjectList(HttpServletRequest request){
        return projectService.loadProjectList(request);
    }

    //하나씩 찾아도 유저가 있어야 하지 않은가?
    //사실 이건 릴리즈노트 기능이다

    @Operation(summary="프로젝트 하나 가져오기", description="프로젝트ID를 가져와 프로젝트를 표시합니다.")
    @GetMapping("/load/one/{projectId}")
    public ProjectContentResponseDTO loadProject(@PathVariable Long projectId){
        return projectService.loadProject(projectId);
    }

    @Operation(summary="프로젝트 하나 가져오기(관리페이지)", description="프로젝트ID를 가져와 프로젝트와 유저정보를 표시합니다.")
    @GetMapping("/manage/{projectId}")
    public ProjectManagementContentResponseDTO loadManagementProject(HttpServletRequest request, @PathVariable Long projectId){
        return projectService.loadManagementProject(request,projectId);
    }

    //멤버 편집
    @Operation(summary="프로젝트 수정", description="프로젝트ID를 받아 프로젝트를 수정합니다. 멤버를 추가하거나 제거할 수 있습니다.")
    @PutMapping("/update/{projectId}")
    public ProjectUpdateRequestDTO updateProject(@PathVariable Long projectId,@RequestBody ProjectUpdateRequestDTO projectUpdateRequestDto){
        return projectService.updateProject(projectId, projectUpdateRequestDto);

    }
    //프로젝트 삭제
    //삭제조건 softDelete
    //조회시 softDelete된 내용은 제외
    @Operation(summary="프로젝트 삭제", description="projectID를 받아 프로젝트를 삭제합니다.")
    @DeleteMapping("/delete/{projectId}")
    public HttpResponse deleteProject(@PathVariable Long projectId){
        return projectService.deleteProject(projectId);
    }


    @GetMapping("/dashboard/meberStatus/{projectId}")
    public List<ProjectUserCheckDTO> userCheck(@PathVariable Long projectId){
        return projectService.checkUser(projectId);
    }


    //검색
    //이름
    //프로젝트 검색
    //인덱스 생성
}
