package com.swave.urnr.user.requestdto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@ApiModel(value = "업데이트 DTO")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateAccountRequestDto {

    @ApiModelProperty(value="사용자 비밀번호", example = "1q2w3e4r!", required = true)
    @ApiParam(value = "비밀번호", required = true, example = "비밀번호를 입력하세요.")
    private String password;
    @ApiModelProperty(value="사용자 소속", example = "가천대학교", required = true)
    @ApiParam(value = "소속", required = true, example = "소속을 입력하세요.")
    private String department;
    @ApiModelProperty(value="사용자 이름", example = "전강훈", required = true)
    @ApiParam(value = "이름", required = true, example = "이름을 입력하세요.")
    private String name;


}
