package com.swave.urnr.user.domain;

import com.swave.urnr.project.domain.Project;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.swave.urnr.Util.type.UserRole;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

//cnrk?asds
@Entity
@Getter
@Setter
@NoArgsConstructor
@Where(clause = "is_deleted = false")
@SQLDelete(sql = "UPDATE user_in_project SET is_deleted = true WHERE user_in_project_id = ?")
public class UserInProject {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_in_project_id")
    private Long id;
    @Column(name = "role")
    private UserRole role; // 구독자, 개발자, 관리자

    // User 와 mapping
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private User user;

    // Project 와 mapping
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;


    @Column(name = "is_deleted")
    private boolean isDeleted = Boolean.FALSE;

    @Builder
    public UserInProject(UserRole role, User user, Project project){
        this.role=role;
        this.user=user;
        this.project=project;
    }


}
