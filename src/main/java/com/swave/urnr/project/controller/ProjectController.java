package com.swave.urnr.project.controller;

import com.swave.urnr.project.exception.NotAuthorizedException;
import com.swave.urnr.project.requestdto.ProjectCreateRequestDTO;
import com.swave.urnr.project.requestdto.ProjectKeywordRequestContentDTO;
import com.swave.urnr.project.requestdto.ProjectUpdateRequestDTO;
import com.swave.urnr.project.responsedto.*;

import com.swave.urnr.project.service.ProjectService;

import com.swave.urnr.util.type.UserRole;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import com.swave.urnr.util.http.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", allowCredentials = "true")
@Slf4j
@RequiredArgsConstructor
@Api(tags = "ProjectController")
public class ProjectController {

    private final ProjectService projectService;

    //0706 : create 생성

    @Operation(summary="프로젝트 생성", description="JWT에서 유저정보를 받아 프로젝트를 생성합니다.")
    @PostMapping("/project")
    public HttpResponse createProject(HttpServletRequest request, @RequestBody ProjectCreateRequestDTO project) throws InterruptedException {;
        return projectService.createProject(request, project);
    }



    @Cacheable(value="loadProjectList")
    @Operation(summary="프로젝트 전체 가져오기", description="JWT에서 유저정보를 받아 해당 유저의 프로젝트 전체를 가져옵니다.")
    @GetMapping("/projects")
    public List<ProjectListResponseDTO> loadProjectList(HttpServletRequest request){
        return projectService.loadProjectList(request);
    }


    @Cacheable(value="loadProject")
    @Operation(summary="프로젝트 하나 가져오기", description="프로젝트 ID를 가져와 프로젝트를 표시합니다.")
    @GetMapping("/project/{projectId}")
    public ProjectContentResponseDTO loadProject(HttpServletRequest request, @PathVariable Long projectId) throws NotAuthorizedException {

        return projectService.loadProject(request, projectId);
    }


    @Cacheable(value="loadManagementProject")
    @Operation(summary="프로젝트 하나 가져오기(관리페이지)", description="프로젝트ID를 가져와 프로젝트와 유저정보를 표시합니다.")
    @GetMapping("/project/{projectId}/manage")
    public ProjectManagementContentResponseDTO loadManagementProject(HttpServletRequest request, @PathVariable Long projectId) throws NotAuthorizedException, IOException {
        //loadManagementProject
        //loadManagementProjectJPA
        return projectService.loadManagementProject(request,projectId);
    }

    @Cacheable(value="searchProject") //ㄱRedis1 캐싱이 필요한 기능에 적용후
    @Operation(summary="프로젝트 검색하기", description="프로젝트 검색결과를 표시합니다.")
    @PostMapping("/project/search")
    public ProjectSearchResultListResponseDTO searchProject(@RequestBody ProjectKeywordRequestContentDTO projectKeywordRequestContentDTO)throws UnsupportedEncodingException {
        //System.out.println(projectKeywordRequestContentDTO.getKeyword());
        return projectService.searchProject(projectKeywordRequestContentDTO);
    }


    @Operation(summary="프로젝트 수정", description="프로젝트ID를 받아 프로젝트를 수정합니다. 멤버를 추가하거나 제거할 수 있습니다.")
    @PutMapping("/project/{projectId}")
    public ProjectUpdateRequestDTO updateProject(HttpServletRequest request,@PathVariable Long projectId,@RequestBody ProjectUpdateRequestDTO projectUpdateRequestDto) throws NotAuthorizedException {
        return projectService.updateProject(request, projectId, projectUpdateRequestDto);

    }

    @Operation(summary="프로젝트 삭제", description="projectID를 받아 프로젝트를 삭제합니다.")
    @DeleteMapping("/project/{projectId}")
    public HttpResponse deleteProject(HttpServletRequest request,@PathVariable Long projectId) throws NotAuthorizedException {

        return projectService.deleteProject(request,projectId);
    }
    @Operation(summary="프로젝트 팀원 온/오프 확인", description="projectID를 받아 프로젝트 각 인원의 현재 온/오프 상태를 확인합니다.")
    @GetMapping("/project/memberStatus/{projectId}")
    public List<ProjectUserCheckDTO> userCheck(@PathVariable Long projectId){
        return projectService.checkUser(projectId);
    }

}
