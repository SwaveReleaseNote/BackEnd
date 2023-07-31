package com.swave.urnr.releasenote.responsedto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@ApiModel(value = "릴리즈 노트 라벨 카운트 리스트 가져오기 DTO")
@NoArgsConstructor
public class ReleaseNoteLabelCountResponseDTO {
    @ApiModelProperty(value="라벨 이름", example = "New", required = true)
    private String label;
    @ApiModelProperty(value="라벨의 개수", example = "27", required = true)
    private Long count;
}
