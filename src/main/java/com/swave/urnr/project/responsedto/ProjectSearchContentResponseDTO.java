package com.swave.urnr.project.responsedto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.swave.urnr.user.responsedto.UserMemberInfoResponseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "프로젝트 검색")
public class ProjectSearchContentResponseDTO {
    @ApiModelProperty(value="프로젝트 ID", example = "1", required = true)
    Long id;

    @ApiModelProperty(value="프로젝트 이름", example = "Wave Form", required = true)
    String name;

    @ApiModelProperty(value="프로젝트 세부사항", example = "설문조사 프로그램", required = true)
    String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @ApiModelProperty(value="프로젝트 생성날짜", example = "자동생성", required = true)
    Date createDate;

    @ApiModelProperty(value="프로젝트 관리자ID", example = "1", required = true)
    Long managerId;

    @ApiModelProperty(value="프로젝트 관리자 이름", example = "Kang", required = true)
    String managerName;

    @ApiModelProperty(value="프로젝트 관리자 부서", example = "개발1팀", required = true)
    String managerDepartment;

    @ApiModelProperty(value="프로젝트 참여자", example = "참가자 리스트", required = true)
    List<UserMemberInfoResponseDTO> teamMembers;

    @Builder
    public ProjectSearchContentResponseDTO(Long id, String name, String description, Date createDate, Long managerId, String managerName, String managerDepartment) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createDate = createDate;
        this.managerId = managerId;
        this.managerName = managerName;
        this.managerDepartment = managerDepartment;
    }
}
