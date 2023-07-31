package com.swave.urnr.project.requestdto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@ApiModel(value = "프로젝트 업데이트 DTO")
public class ProjectUpdateRequestDTO {

    @ApiModelProperty(value="프로젝트 이름", example = "Wave Form", required = true)
    String name;

    @ApiModelProperty(value="프로젝트 속성", example = "설문조사 프로그램", required = true)
    String description;

    @ApiModelProperty(value="프로젝트 제외 명단", example = "[4,5]", required = true)
    List<Long> deleteUsers;

    @ApiModelProperty(value="프로젝트 포함 명단", example = "[6]", required = true)
    List<Long> updateUsers;

    @Builder
    public ProjectUpdateRequestDTO( String name, String description, List<Long> deleteUsers, List<Long> updateUsers) {

        this.name = name;
        this.description = description;
        this.deleteUsers = deleteUsers;
        this.updateUsers = updateUsers;
    }
}
