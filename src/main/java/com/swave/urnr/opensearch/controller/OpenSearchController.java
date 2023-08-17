package com.swave.urnr.opensearch.controller;

import com.swave.urnr.opensearch.domain.ProjectOpenSearch;
import com.swave.urnr.opensearch.domain.UserOpenSearch;
import com.swave.urnr.opensearch.service.OpenSearchIndexService;
import com.swave.urnr.opensearch.service.OpenSearchService;
import com.swave.urnr.project.responsedto.ProjectSearchResultListResponseDTO;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Api(tags = "OpenSearchController")
@CrossOrigin(origins = "http://61.109.214.110:80", allowedHeaders = "*", allowCredentials = "true")
public class OpenSearchController {

    private final OpenSearchService openSearchService;
    private final OpenSearchIndexService openSearchIndexService;

    @Operation(summary="검색 쿼리 테스트", description="테스트용 코드입니다.")
    @GetMapping("/test/{query}")
    public List<UserOpenSearch> test(@PathVariable Long query){
        //List<ProjectOpenSearch> test = openSearchService.searchProjectByDescription(query);
        //List<ProjectOpenSearch> test = openSearchService.searchProjectByProjectName(query);
        List<UserOpenSearch> test = openSearchService.searchUserById(query);
        //List<UserInProjectOpenSearch> test = openSearchService.searchUserInProjectByRole(2L);
        return test;
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

    @Operation(summary="프로젝트 총검색 쿼리 테스트", description="테스트용 코드입니다.")
    @PostMapping("/api/search/{keyword}/open")
    public ProjectSearchResultListResponseDTO searchProject(@PathVariable String keyword){
        return openSearchService.searchProject(keyword);
    }
}