package com.swave.urnr.project.domain;

import com.swave.urnr.project.responsedto.ProjectSearchContentResponseDTO;
import com.swave.urnr.project.responsedto.ProjectSearchListResponseDTO;
import com.swave.urnr.releasenote.domain.ReleaseNote;
import com.swave.urnr.user.domain.User;
import com.swave.urnr.user.domain.UserInProject;
import com.swave.urnr.user.responsedto.UserMemberInfoResponseDTO;
import jdk.jfr.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.swave.urnr.util.type.UserRole.Manager;
import static java.lang.Boolean.FALSE;

//tosting 에러가 나면 @data를 게터세터로 바꾼다

@Entity
@Data
@NoArgsConstructor
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
    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY,orphanRemoval = true)
    @Column(name = "user_in_project_id")
    private List<UserInProject> userInProjectList;

    // project 와 release note mapping
    @OneToMany(mappedBy = "project",orphanRemoval = true)
    @Column(name = "release_note_id")
    private List<ReleaseNote> releaseNoteList;

    @Column(name = "is_deleted")
    private boolean isDeleted = Boolean.FALSE;


    @Builder
    public Project(String name, String description, Date createDate, List<UserInProject> userInProjectList) {
        this.name = name;
        this.description = description;
        this.createDate = createDate;
        this.userInProjectList = userInProjectList;
    }

    public static List<ProjectSearchContentResponseDTO> makeProjectSearchListResponseDTOList(List<ProjectSearchListResponseDTO> projectSearchListResponseDTOList) {
        List<ProjectSearchContentResponseDTO> projectSearchResponseDTOList = new ArrayList<>();
        //진짜신기하다 어떻게 메니저가 안바뀌고 들어가지?
        //코드이해중
        for(ProjectSearchListResponseDTO projectSearchListResponseDTO : projectSearchListResponseDTOList){

            Optional<ProjectSearchContentResponseDTO> existingProject = projectSearchResponseDTOList.stream()
                    .filter(p -> p.getId().equals(projectSearchListResponseDTO.getId()))
                    .findFirst();
            if(existingProject.isPresent()){
                existingProject.get().getTeamMembers().add(new UserMemberInfoResponseDTO(projectSearchListResponseDTO.getUserId(),projectSearchListResponseDTO.getUserName(),projectSearchListResponseDTO.getUserDepartment()));
            }else{
                ProjectSearchContentResponseDTO newProject = ProjectSearchContentResponseDTO.builder()
                        .id(projectSearchListResponseDTO.getId())
                        .name(projectSearchListResponseDTO.getName())
                        .description(projectSearchListResponseDTO.getDescription())
                        .createDate(projectSearchListResponseDTO.getCreateDate())
                        .managerId(projectSearchListResponseDTO.getUserId())
                        .managerName(projectSearchListResponseDTO.getUserName())
                        .managerDepartment(projectSearchListResponseDTO.getUserDepartment())
                        .build();
                List<UserMemberInfoResponseDTO> teamMembers = new ArrayList<>();
                if(projectSearchListResponseDTO.getUserRole()!=Manager) {
                    teamMembers.add(new UserMemberInfoResponseDTO(projectSearchListResponseDTO.getUserId(), projectSearchListResponseDTO.getUserName(), projectSearchListResponseDTO.getUserDepartment()));
                }
                newProject.setTeamMembers(teamMembers);
                projectSearchResponseDTOList.add(newProject);

            }
        }
        return projectSearchResponseDTOList;
    }

}
