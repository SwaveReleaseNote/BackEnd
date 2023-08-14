package com.swave.urnr.opensearch.service;

import com.swave.urnr.opensearch.domain.ProjectOpenSearch;
import com.swave.urnr.opensearch.domain.UserInProjectOpenSearch;
import com.swave.urnr.opensearch.domain.UserOpenSearch;
import com.swave.urnr.project.responsedto.ProjectSearchListResponseDTO;
import com.swave.urnr.project.responsedto.ProjectSearchResultListResponseDTO;
import com.swave.urnr.util.type.UserRole;
import org.elasticsearch.action.search.SearchResponse;

import java.io.IOException;
import java.util.List;

public interface OpenSearchService {
    //List<ProjectOpenSearch> searchProjectByDescription(String description);
    List<ProjectOpenSearch> searchProjectByProjectName(String name);
    List<UserInProjectOpenSearch> searchUserInProjectByRole(Long roleId);

    List<UserInProjectOpenSearch> searchUserInProjectByProjectId(Long projectId);

    List<UserOpenSearch> searchUserById(Long userId);

    //이름 검색 만들기
    List<UserOpenSearch> searchUserByName(String name);



    List<ProjectOpenSearch> searchProjectByDescription(String keyword);

    ProjectSearchResultListResponseDTO searchProject(String keyword);
}
