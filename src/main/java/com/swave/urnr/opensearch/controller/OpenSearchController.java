package com.swave.urnr.opensearch.controller;

import com.swave.urnr.opensearch.domain.ProjectOpenSearch;
import com.swave.urnr.opensearch.domain.UserInProjectOpenSearch;
import com.swave.urnr.opensearch.domain.UserOpenSearch;
import com.swave.urnr.opensearch.service.OpenSearchIndexService;
import com.swave.urnr.opensearch.service.OpenSearchService;
import com.swave.urnr.project.responsedto.ProjectSearchListResponseDTO;
import com.swave.urnr.project.responsedto.ProjectSearchResultListResponseDTO;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Api(tags = "OpenSearchController")
public class OpenSearchController {

    private final OpenSearchService openSearchService;
    private final OpenSearchIndexService openSearchIndexService;

    @Operation(summary="프로젝트 검색 쿼리 테스트", description="테스트용 코드입니다.")
    @GetMapping("/test/{keyword}")
    public List<ProjectOpenSearch> test(@PathVariable String keyword){
        //List<ProjectOpenSearch> test = openSearchService.searchProjectByProjectName(query);
        //List<UserOpenSearch> test = openSearchService.searchUserById(1L);
        //List<UserInProjectOpenSearch> test = openSearchService.searchUserInProjectByRole(2L);
        return openSearchService.searchProjectByDescription(keyword);
    }

    @Operation(summary="유인프 검색 쿼리 테스트", description="테스트용 코드입니다.")
    @GetMapping("/test/userInProject/{Role}")
    public List<UserInProjectOpenSearch> test2(@PathVariable Long Role){
        return openSearchService.searchUserInProjectByRole(Role);
    }
    @Operation(summary="유저 검색 쿼리 테스트", description="테스트용 코드입니다.")
    @GetMapping("/test/user/{Id}")
    public List<UserOpenSearch> test3(@PathVariable Long Id){
        return openSearchService.searchUserById(Id);
    }

    @Operation(summary="프로젝트 이름 검색 쿼리 테스트", description="테스트용 코드입니다.")
    @GetMapping("/test/projectName/{keyword}")
    public List<ProjectOpenSearch> test4(@PathVariable String keyword){
        return openSearchService.searchProjectByProjectName(keyword);
    }

    @Operation(summary="프로젝트 총검색 쿼리 테스트", description="테스트용 코드입니다.")
    @PostMapping("/test/search/{keyword}")
    public ProjectSearchResultListResponseDTO test5(@PathVariable String keyword){
        return openSearchService.searchProject(keyword);
    }


    @Operation(summary="유저 인덱스 생성", description="OpenSearch에 유저 인덱스를 생성합니다.")
    @GetMapping("/api/opensearch/index/user")
    public String createUserIndex() throws IOException {
        return openSearchIndexService.createUserIndex();
    }

    @Operation(summary="유저인프로젝트 인덱스 생성", description="OpenSearch에 유저인프로젝트 인덱스를 생성합니다.")
    @GetMapping("/api/opensearch/index/user-in-project")
    public String createUserInProjectIndex() throws IOException {
        return openSearchIndexService.createUserInProjectIndex();
    }

    @Operation(summary="프로젝트 인덱스 생성", description="OpenSearch에 프로젝트 인덱스를 생성합니다.")
    @GetMapping("/api/opensearch/index/project")
    public String createProjectIndex() throws IOException {
        return openSearchIndexService.createProjectIndex();
    }
}
