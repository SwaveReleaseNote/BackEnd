package com.swave.urnr.user.controller;

import com.swave.urnr.project.exception.NotAuthorizedException;
import com.swave.urnr.user.domain.UserInProject;
import com.swave.urnr.user.service.UserInProjectService;
import com.swave.urnr.util.http.HttpResponse;
import com.swave.urnr.util.type.UserRole;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Api(tags = "UserInProjectController")
@RequestMapping("/api/project")
@RestController
@CrossOrigin
@Slf4j
@RequiredArgsConstructor
public class UserInProjectController {

    private final UserInProjectService userInProjectService;

    //프로젝트 탈퇴
    //softDelete
    @Operation(summary = "프로젝트 탈퇴(개발자, 구독자)", description="projectID를 받아 프로젝트에서 탈퇴합니다.")
    @DeleteMapping("/{projectId}/drop")
    public HttpResponse dropProject(HttpServletRequest request, @PathVariable Long projectId){
        return userInProjectService.dropProject(request, projectId);
    }


    @Operation(summary = "프로젝트 구독", description="projectID와 유저정보를 받아 프로젝트를 구독합니다.")
    @PostMapping("/{projectId}/subscribe")
    public HttpResponse subscribeProject(HttpServletRequest request, @PathVariable Long projectId) throws NotAuthorizedException {
        return userInProjectService.subscribeProject(request, projectId);
    }


    @Cacheable(value="getRole")
    @Operation(summary = "유저 권한 확인", description="projectID와 유저정보를 받아 권한을 확인합니다.")
    @GetMapping("/{projectId}/role")
    public UserRole getRole(HttpServletRequest request, @PathVariable Long projectId){
        return userInProjectService.getRole(request, projectId);
    }


}
