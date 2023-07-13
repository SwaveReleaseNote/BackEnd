package com.swave.releasenotesharesystem.Project.requestDto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class ProjectUpdateRequestDto {
    Long id;
    String name;
    String description;
    List<Long> deleteUsers;
    List<Long> updateUsers;

    public ProjectUpdateRequestDto(Long id, String name, String description, List<Long> deleteUsers, List<Long> updateUsers) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.deleteUsers = deleteUsers;
        this.updateUsers = updateUsers;
    }
}
