package com.swave.urnr.project.responsedto;

import com.swave.urnr.util.type.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = "is_deleted = false")
@SQLDelete(sql = "UPDATE project SET is_deleted = true WHERE project_id = ?")
public class ProjectListResponseDTO {
    Long id;
    UserRole role;
    String name;
    String description;
    Date createDate;
    int count;
    String version;
    //todo releasenote 나중에 구현할것
    //todo usercount 나중에 구현할것

}
