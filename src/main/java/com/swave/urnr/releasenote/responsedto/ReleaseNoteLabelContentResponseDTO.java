package com.swave.urnr.releasenote.responsedto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@ApiModel(value = "릴리즈 노트 라벨 필터링 데이터 가져오기 DTO")
@NoArgsConstructor
public class ReleaseNoteLabelContentResponseDTO {
    @ApiModelProperty(value="해당 릴리즈 노트 ID", example = "1", required = true)
    private Long releaseNoteId;
    @ApiModelProperty(value="라벨 이름", example = "New", required = true)
    private String label;
    @ApiModelProperty(value="버전", example = "1.0.0", required = true)
    private String version;
    @ApiModelProperty(value="해당 라벨의 내용", example = "intelliJ의 UI를 조정했습니다.", required = true)
    private List<String> context;
}
