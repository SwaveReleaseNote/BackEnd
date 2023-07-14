package com.swave.urnr.project.requestdto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectCreateRequestDTO {
    String projectName;
    String description;
    Date createDate;

    //managerID
    Long userId;
    List<Long> users;


    public ProjectCreateRequestDTO(String projectName, String description, Date createDate) {

        this.projectName = projectName;
        this.description = description;
        this.createDate = createDate;

    }


}
