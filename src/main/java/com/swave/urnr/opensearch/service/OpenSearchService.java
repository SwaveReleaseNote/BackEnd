package com.swave.urnr.opensearch.service;

import com.swave.urnr.opensearch.domain.ProjectOpenSearch;
import com.swave.urnr.opensearch.domain.UserInProjectOpenSearch;
import com.swave.urnr.opensearch.domain.UserOpenSearch;
import com.swave.urnr.util.type.UserRole;
import org.elasticsearch.action.search.SearchResponse;

import java.io.IOException;
import java.util.List;

public interface OpenSearchService {
    List<ProjectOpenSearch> searchProjectByDescription(String description);
    List<ProjectOpenSearch> searchProjectByProjectName(String name);
    List<UserInProjectOpenSearch> searchUserInProjectByRole(Long roleId);
    List<UserOpenSearch> searchUserById(Long userId);
}
