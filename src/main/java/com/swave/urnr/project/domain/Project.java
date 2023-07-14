package com.swave.urnr.project.domain;

import com.swave.urnr.ReleaseNote.domain.ReleaseNote;
import com.swave.urnr.user.domain.UserInProject;
import jdk.jfr.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

//tosting 에러가 나면 @data를 게터세터로 바꾼다

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "is_deleted = false")
@SQLDelete(sql = "UPDATE project SET is_deleted = true WHERE project_id = ?")
public class Project {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private Long id;
    @Column(name = "project_name")
    private String name;
    @Column(columnDefinition = "TEXT", name = "description")
    private String description;
    @Column(name = "create_date") @Timestamp // 자동 생성 yyyy-mm-dd
    private Date createDate;

    // Project 와 userInProject mapping
    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY,cascade = {CascadeType.ALL},orphanRemoval = true)
    @Column(name = "user_in_project_id")
    private List<UserInProject> userInProjectList;

    // project 와 release note mapping
    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY,cascade = {CascadeType.ALL},orphanRemoval = true)
    @Column(name = "release_note_id")
    private List<ReleaseNote> releaseNoteList;

    @Column(name = "is_deleted")
    private boolean isDeleted = Boolean.FALSE;


    @Builder
    public Project(String name, String description, Date createDate) {
        this.name = name;
        this.description = description;
        this.createDate = createDate;
    }
}
