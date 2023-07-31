package com.swave.urnr.user.controller;

import com.swave.urnr.user.domain.UserInProject;
import com.swave.urnr.user.service.UserInProjectService;
import com.swave.urnr.util.http.HttpResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    @DeleteMapping("/drop/{projectId}")
    public HttpResponse dropProject(HttpServletRequest request, @PathVariable Long projectId){
        return userInProjectService.dropProject(request, projectId);
    }
}
