package com.swave.urnr.user.responsedto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "응답 DTO")
public interface responseDTO {

    @ApiModelProperty(value="사용자 식별번호", example = "1", required = true)
    Long getUser_id();

    @ApiModelProperty(value="사용자 이름", example = "전강훈", required = true)
    String getUser_name();

    @ApiModelProperty(value="사용자 소속", example = "가천대학교", required = true)
    String getDepartment();

}