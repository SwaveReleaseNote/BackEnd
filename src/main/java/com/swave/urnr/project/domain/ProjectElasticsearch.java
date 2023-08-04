package com.swave.urnr.project.domain;

import com.swave.urnr.user.responsedto.UserMemberInfoResponseDTO;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Date;
import java.util.List;

@Data
@Document(indexName = "project_index", shards = 1)
public class ProjectElasticsearch {

    @Id
    private Long id;
    private String name;
    private String description;
    private Date createDate;
    private Long managerId;
    private String managerName;
    private String managerDepartment;
    private List<UserMemberInfoResponseDTO> teamMembers;
    private List<Long> userInProjectIds;
    private List<Long> releaseNoteIds;

    // Empty constructor and builder

    @Builder
    public ProjectElasticsearch(Long id, String name, String description, Date createDate, Long managerId, String managerName, String managerDepartment, List<UserMemberInfoResponseDTO> teamMembers, List<Long> userInProjectIds, List<Long> releaseNoteIds) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createDate = createDate;
        this.managerId = managerId;
        this.managerName = managerName;
        this.managerDepartment = managerDepartment;
        this.teamMembers = teamMembers;
        this.userInProjectIds = userInProjectIds;
        this.releaseNoteIds = releaseNoteIds;
    }
}
