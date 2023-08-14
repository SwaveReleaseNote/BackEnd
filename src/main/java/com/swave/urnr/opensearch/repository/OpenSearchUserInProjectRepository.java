package com.swave.urnr.opensearch.repository;

import com.swave.urnr.opensearch.domain.ProjectOpenSearch;
import com.swave.urnr.opensearch.domain.UserInProjectOpenSearch;
import com.swave.urnr.opensearch.domain.UserOpenSearch;
import com.swave.urnr.util.type.UserRole;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface OpenSearchUserInProjectRepository extends ElasticsearchRepository<UserInProjectOpenSearch, Long> {
    List<UserInProjectOpenSearch> findByRole(Long roleId);
}