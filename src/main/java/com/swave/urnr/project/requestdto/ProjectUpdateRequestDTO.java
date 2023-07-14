package com.swave.urnr.project.requestdto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ProjectUpdateRequestDTO {
    Long id;
    String name;
    String description;
    List<Long> deleteUsers;
    List<Long> updateUsers;

    public ProjectUpdateRequestDTO(Long id, String name, String description, List<Long> deleteUsers, List<Long> updateUsers) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.deleteUsers = deleteUsers;
        this.updateUsers = updateUsers;
    }
}
