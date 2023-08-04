package com.swave.urnr.user.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@Document(indexName = "user_index", shards = 1)
public class UserElasticsearch {

    @Id
    private Long id;
    private String email;
    private String department;
    private String username;
    private String provider;
    private boolean isDeleted;

    // Empty constructor and builder

    @Builder
    public UserElasticsearch(Long id, String email, String department, String username, String provider, boolean isDeleted) {
        this.id = id;
        this.email = email;
        this.department = department;
        this.username = username;
        this.provider = provider;
        this.isDeleted = isDeleted;
    }
}
