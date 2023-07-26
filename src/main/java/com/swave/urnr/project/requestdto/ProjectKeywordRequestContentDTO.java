package com.swave.urnr.project.requestdto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "프로젝트 검색 DTO")
public class ProjectKeywordRequestContentDTO {
    
    @ApiModelProperty(value="검색 키워드 입력", example = "Wave Form", required = true)
    String keyword;
}
