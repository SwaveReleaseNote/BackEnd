package com.swave.urnr.user.requestdto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;

/*
회원가입상에서 Email / Username / Password 세개 모두 필요하기 떄문에, 모두 입력받음.
이후 첫 로그인시 Data를 받아야 함.
 TODO : Email이 valid한지 알아보아야 하므로 유효성 검사를 위해 남길지, 또는 유효성 검사를 포기하고 사용자를 신뢰하여 String 형태로 구현할지 결정필요
 */
@Data
@ApiModel(value = "이메일 DTO")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class UserValidateEmailDTO {
    @Email
    @ApiModelProperty(value="사용자 이메일", example = "artisheep@naver.com", required = true)
    @ApiParam(value = "이메일", required = true, example = "이메일을 입력하세요.")
    private String email;

}

