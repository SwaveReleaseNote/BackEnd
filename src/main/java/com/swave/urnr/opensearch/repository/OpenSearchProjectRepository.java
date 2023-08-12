package com.swave.urnr.opensearch.repository;

import com.swave.urnr.opensearch.domain.ProjectOpenSearch;
import com.swave.urnr.opensearch.domain.UserInProjectOpenSearch;
import com.swave.urnr.opensearch.domain.UserOpenSearch;
import com.swave.urnr.util.type.UserRole;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OpenSearchProjectRepository extends ElasticsearchRepository<ProjectOpenSearch, Long> {
    @Query("{\"bool\": " +
            "{\"must\": [" +
            "{\"match\": {\"is_deleted\": false}}], " +
            "\"should\": [" +
            "{\"match\": {\"description\": \"?0\"}}," +
            "{\"match\": {\"description.nori\": \"?0\"}}," +
            "{\"match\": {\"description.ngram\": \"?0\"}}" +
            "]" +
            "}" +
            "}")
    List<ProjectOpenSearch> findByDescription(String description);

    @Query("{\"bool\": " +
            "{\"must\": " +
            "{\"match\": {\"is_deleted\": false}}, " +
            "\"should\": [" +
            "{\"match\": {\"project_name\": \"?0\"}}," +
            "{\"match\": {\"project_name.nori\": \"?0\"}}," +
            "{\"match\": {\"project_name.ngram\": \"?0\"}}" +
            "]" +
            "}" +
            "}")
    List<ProjectOpenSearch> findByProjectName(String name);
}