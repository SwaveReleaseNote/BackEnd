package com.swave.urnr.opensearch.service;

import com.swave.urnr.opensearch.domain.ProjectOpenSearch;
import com.swave.urnr.opensearch.domain.UserInProjectOpenSearch;
import com.swave.urnr.opensearch.domain.UserOpenSearch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenSearchServiceImpl implements OpenSearchService{

    private final ElasticsearchOperations elasticsearchOperations;
    @Override
    public List<ProjectOpenSearch> searchProjectByDescription(String description){
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(
                        QueryBuilders.boolQuery()
                                .must(QueryBuilders.matchQuery("is_deleted", false))
                                .should(QueryBuilders.matchQuery("description", description))
                                .should(QueryBuilders.matchQuery("description.nori", description))
                                .should(QueryBuilders.matchQuery("description.ngram", description)))
                .withMinScore(0.3F)
                .build();

        SearchHits<ProjectOpenSearch> searchResult =  elasticsearchOperations.search(searchQuery, ProjectOpenSearch.class);

        return new ArrayList<>(searchResult.stream().map(org.springframework.data.elasticsearch.core.SearchHit::getContent).toList());
    }

    @Override
    public List<ProjectOpenSearch> searchProjectByProjectName(String name) {
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(
                QueryBuilders.boolQuery()
                        .must(QueryBuilders.matchQuery("is_deleted", false))
                        .should(QueryBuilders.matchQuery("project_name", name))
                        .should(QueryBuilders.matchQuery("project_name.nori", name))
                        .should(QueryBuilders.matchQuery("project_name.ngram", name)))
                .withMinScore(0.3F)
                .build();

        SearchHits<ProjectOpenSearch> test =  elasticsearchOperations.search(searchQuery, ProjectOpenSearch.class);

        return new ArrayList<>(test.stream().map(org.springframework.data.elasticsearch.core.SearchHit::getContent).toList());
    }

    @Override
    public List<UserInProjectOpenSearch> searchUserInProjectByRole(Long roleId) {
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(
                        QueryBuilders.boolQuery()
                                .must(QueryBuilders.matchQuery("is_deleted", false))
                                .must(QueryBuilders.matchQuery("role", roleId)))
                .build();

        SearchHits<UserInProjectOpenSearch> searchResult =  elasticsearchOperations.search(searchQuery, UserInProjectOpenSearch.class);

        return new ArrayList<>(searchResult.stream().map(org.springframework.data.elasticsearch.core.SearchHit::getContent).toList());
    }

    @Override
    public List<UserOpenSearch> searchUserById(Long userId) {
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(
                        QueryBuilders.boolQuery()
                                .must(QueryBuilders.matchQuery("is_deleted", false))
                                .must(QueryBuilders.matchQuery("user_id", userId)))
                .build();

        SearchHits<UserOpenSearch> searchResult =  elasticsearchOperations.search(searchQuery, UserOpenSearch.class);

        return new ArrayList<>(searchResult.stream().map(org.springframework.data.elasticsearch.core.SearchHit::getContent).toList());
    }
}
