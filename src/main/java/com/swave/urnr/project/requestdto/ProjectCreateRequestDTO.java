package com.swave.urnr.project.requestdto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "프로젝트 생성 DTO")
public class ProjectCreateRequestDTO {

    @ApiModelProperty(value="프로젝트 이름", example = "Wave Form", required = true)
    String projectName;

    @ApiModelProperty(value="프로젝트 세부사항", example = "설문조사 프로그램", required = true)
    String description;


    //managerID
    @ApiModelProperty(value="사용자 ID", example = "1", required = true)
    Long userId;

    @ApiModelProperty(value="프로젝트에 담긴 유저", example = "[2,3,4,5]", required = true)
    List<Long> users;


    @Builder
    public ProjectCreateRequestDTO(String projectName, String description) {

        this.projectName = projectName;
        this.description = description;


    }



}
