package com.swave.urnr.user.requestdto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.*;

@ApiModel(value = "로그인 요청용 DTO")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserLoginServerRequestDTO {

    @ApiModelProperty(value="사용자 이메일", example = "artisheep@naver.com", required = true)
    @ApiParam(value = "이메일", required = true, example = "이메일을 입력하세요.")
    private String email;
    @ApiModelProperty(value="사용자 비밀번호", example = "1q2w3e4r!", required = true)
    @ApiParam(value = "비밀번호", required = true, example = "비밀번호를 입력하세요.")
    private String password;

}