package com.swave.urnr.project.responsedto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = "is_deleted = false")
@SQLDelete(sql = "UPDATE project SET is_deleted = true WHERE project_id = ?")
@ApiModel(value = "프로젝트 하나 가져오는 DTO")
@Builder
public class ProjectContentResponseDTO {

    @ApiModelProperty(value="프로젝트 ID", example = "1", required = true)
    Long id;

    @ApiModelProperty(value="프로젝트 이름", example = "Wave Form", required = true)
    String name;

    @ApiModelProperty(value="프로젝트 세부사항", example = "설문조사 프로그램", required = true)
    String description;

    @ApiModelProperty(value="프로젝트 생성날짜", example = "자동생성", required = true)
    Date createDate;





}
