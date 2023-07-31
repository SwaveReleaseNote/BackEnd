package com.swave.urnr.project.responsedto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import com.swave.urnr.util.type.UserRole;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@ApiModel(value = "프로젝트 검색 결과 가져오는 DTO")
public class ProjectSearchListResponseDTO {

    @ApiModelProperty(value="프로젝트 ID", example = "1", required = true)
    Long id;

    @ApiModelProperty(value="프로젝트 이름", example = "Wave Form", required = true)
    String name;

    @ApiModelProperty(value="프로젝트 세부사항", example = "설문조사 프로그램", required = true)
    String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @ApiModelProperty(value="프로젝트 생성날짜", example = "자동생성", required = true)
    Date createDate;

    @ApiModelProperty(value="프로젝트 참여자 ID", example = "1", required = true)
    Long userId;

    @ApiModelProperty(value="프로젝트 참여자 이름", example = "Kang", required = true)
    String userName;

    @ApiModelProperty(value="프로젝트 참여자 부서", example = "개발1팀", required = true)
    String userDepartment;

    @ApiModelProperty(value="프로젝트 참여자 역할", example = "Developer", required = true)
    UserRole userRole;

    //List<UserMemberInfoResponseDTO> teamMembers;


    @QueryProjection
    public ProjectSearchListResponseDTO(Long id, String name, String description, Date createDate, Long userId, String userName, String userDepartment, UserRole userRole) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createDate = createDate;
        this.userId = userId;
        this.userName = userName;
        this.userDepartment = userDepartment;
        this.userRole = userRole;
    }
}
