package com.swave.urnr.project.responsedto;

import com.swave.urnr.Util.type.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
