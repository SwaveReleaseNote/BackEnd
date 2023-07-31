package com.swave.urnr.user.responsedto;


import com.querydsl.core.annotations.QueryProjection;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@ApiModel(value = "사용자 멤버 정보 응답 DTO")
@Data
@NoArgsConstructor
public class UserMemberInfoResponseDTO {

    @ApiModelProperty(value = "사용자 ID", example = "1", required = true)
    Long userId;

    @ApiModelProperty(value="사용자 이름", example = "전강훈", required = true)
    String username;

    @ApiModelProperty(value="사용자 소속", example = "가천대학교", required = true)
    String userDepartment;



    @QueryProjection

    public UserMemberInfoResponseDTO(Long userId, String username, String userDepartment) {
        this.userId = userId;
        this.username = username;
        this.userDepartment = userDepartment;
    }
}
