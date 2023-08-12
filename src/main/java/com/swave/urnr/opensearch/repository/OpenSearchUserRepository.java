package com.swave.urnr.opensearch.repository;

import com.swave.urnr.opensearch.domain.ProjectOpenSearch;
import com.swave.urnr.opensearch.domain.UserInProjectOpenSearch;
import com.swave.urnr.opensearch.domain.UserOpenSearch;
import com.swave.urnr.user.domain.User;
import com.swave.urnr.util.type.UserRole;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface OpenSearchUserRepository extends ElasticsearchRepository<UserOpenSearch, Long>{
        @Query("{\"bool\": {\"must\": [{\"match\": {\"is_deleted\": false}}, {\"match\": {\"user_id\": \"?0\"}}]}}")
    public List<UserOpenSearch> findByUserId(Long userId);
}
