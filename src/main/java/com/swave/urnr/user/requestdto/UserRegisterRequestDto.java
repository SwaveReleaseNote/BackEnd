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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/*
TODO: Find how to implement NOTNULL VALIDATE without hardcoding, also need to find Email validation system with RNG

 */
@Data
@ApiModel(value = "회원가입 DTO")
@Validated
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterRequestDto {
    @NotNull
    @Email
    @NotBlank
    @ApiModelProperty(value="사용자 이메일", example = "artisheep@naver.com", required = true)
    @ApiParam(value = "이메일", required = true, example = "이메일을 입력하세요.")
    private String email;
    @NotNull
    @NotBlank
    @ApiModelProperty(value="사용자 비밀번호", example = "1q2w3e4r!", required = true)
    @ApiParam(value = "비밀번호", required = true, example = "비밀번호를 입력하세요.")
    private String password;
    @NotNull
    @NotBlank
    @ApiModelProperty(value="사용자 이름", example = "전강훈", required = true)
    @ApiParam(value = "사용자 이름", required = true, example = "이름을 입력하세요.")
    private String name;


}

