package com.swave.urnr.project.responsedto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@ApiModel(value = "프로젝트 검색 결과")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectSearchResultListResponseDTO {
    @ApiModelProperty(value="프로젝트 제목 검색 결과", example = "1", required = true)
    List<ProjectSearchContentResponseDTO> titleSearch;
    @ApiModelProperty(value="프로젝트 Description 검색 결과", example = "1", required = true)
    List<ProjectSearchContentResponseDTO> descriptionSearch;

    @ApiModelProperty(value="해당 메니저가 참여하는 프로젝트", example = "1", required = true)
    List<ProjectSearchContentResponseDTO> managerSearch;

    @ApiModelProperty(value="해당 개발자가 참여하는 프로젝투", example = "1", required = true)
    List<ProjectSearchContentResponseDTO> developerSearch;

}
