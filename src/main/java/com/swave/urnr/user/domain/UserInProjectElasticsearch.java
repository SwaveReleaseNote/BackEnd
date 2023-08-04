package com.swave.urnr.user.domain;

import com.swave.urnr.project.domain.Project;
import com.swave.urnr.util.type.UserRole;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.Id;

@Data
@Document(indexName = "user_in_project_index",shards = 1)
public class UserInProjectElasticsearch {

    @Id
    private Long id;

    private UserRole role;

    private Long userId;

    private Long projectId;

    private boolean isDeleted = Boolean.FALSE;

    @Builder
    public UserInProjectElasticsearch(Long id, UserRole role, Long userId, Long projectId, boolean isDeleted) {
        this.id = id;
        this.role = role;
        this.userId = userId;
        this.projectId = projectId;
        this.isDeleted = isDeleted;
    }
}
