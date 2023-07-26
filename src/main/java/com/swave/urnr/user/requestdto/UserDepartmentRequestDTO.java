package com.swave.urnr.user.requestdto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.*;

@ApiModel(value = "회원소속 입력 요청용 DTO")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserDepartmentRequestDTO {

    @ApiModelProperty(value="사용자 소속", example = "가천대학교", required = true)
    @ApiParam(value = "소속", required = true, example = "소속을 입력하세요.")
    private String department;

}
