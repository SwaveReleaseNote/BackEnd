package com.swave.urnr.user.requestdto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;

@ApiModel(value = "이메일 인증번호 전송용 DTO")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Validated
public class UserValidateEmailDTO {
    @Email
    @ApiModelProperty(value="사용자 이메일", example = "artisheep@naver.com", required = true)
    @ApiParam(value = "이메일", required = true, example = "이메일을 입력하세요.")
    private String email;

}

