package com.swave.releasenotesharesystem.Project.requestDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectCreateRequestDto {
    String name;
    String description;
    Date createDate;

    //managerID
    Long userId;
    List<Long> users;


    public ProjectCreateRequestDto(String name, String description, Date createDate) {

        this.name = name;
        this.description = description;
        this.createDate = createDate;

    }


}
