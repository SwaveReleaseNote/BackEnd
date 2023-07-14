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
    String name;
    String description;
    Date createDate;

    //managerID
    Long userId;
    List<Long> users;


    public ProjectCreateRequestDTO(String name, String description, Date createDate) {

        this.name = name;
        this.description = description;
        this.createDate = createDate;

    }


}
