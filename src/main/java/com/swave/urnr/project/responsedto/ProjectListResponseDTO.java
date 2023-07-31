package com.swave.urnr.project.responsedto;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.swave.urnr.util.type.UserRole;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "프로젝트 싹다 가져오는 DTO")
public class ProjectListResponseDTO {

    @ApiModelProperty(value="프로젝트 ID", example = "1", required = true)
    Long id;

    @ApiModelProperty(value="팀원 역할", example = "0:구독자 1:개발자 2:매니저", required = true)
    UserRole role;

    @ApiModelProperty(value="프로젝트 제목", example = "Wave Form", required = true)
    String name;

    @ApiModelProperty(value="프로젝트 세부사항", example = "설문조사 프로그램", required = true)
    String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @ApiModelProperty(value="프로젝트 생성날짜", example = "자동생성", required = true)
    Date createDate;

    @ApiModelProperty(value="프로젝트 멤버수", example = "5", required = true)
    int count;

    @ApiModelProperty(value="프로젝트 최신버전", example = "3.5", required = true)
    String version;

}
