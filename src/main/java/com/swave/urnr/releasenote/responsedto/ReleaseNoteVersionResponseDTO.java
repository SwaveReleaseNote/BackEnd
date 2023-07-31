package com.swave.urnr.releasenote.responsedto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@ApiModel(value = "릴리즈 노트 버전 DTO")
@NoArgsConstructor
@AllArgsConstructor
public class ReleaseNoteVersionResponseDTO {
    @ApiModelProperty(value="릴리즈 노트 버전", example = "1.0.0", required = true)
    private String version;
    @ApiModelProperty(value="릴리즈 노트 ID", example = "2", required = true)
    private Long releaseNoteId;
}
