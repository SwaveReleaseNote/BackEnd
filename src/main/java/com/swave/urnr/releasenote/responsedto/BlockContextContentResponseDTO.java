package com.swave.urnr.releasenote.responsedto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@ApiModel(value = "블럭 컨텍스트 작성 DTO")
@NoArgsConstructor
public class BlockContextContentResponseDTO {
    @ApiModelProperty(value="태그", example = "H1", required = true)
    private String tag;

    @ApiModelProperty(value="블럭 컨텍스트 내용", example = "안녕", required = true)
    private String context;

    @ApiModelProperty(value="index", example = "2", required = true)
    private Long index;
}
