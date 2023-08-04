package com.swave.urnr.util.elasticSearch;

import groovyjarjarantlr4.v4.runtime.misc.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

@NoRepositoryBean
public interface ElasticsearchRepository<T,ID> extends PagingAndSortingRepository<T,ID> {
    Page<T> searchSimilar(T entity, @Nullable String[] fields, Pageable pageable);
}
