package com.swave.urnr.releasenote.responsedto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@ApiModel(value = "노트 블럭 작성 DTO")
@NoArgsConstructor
public class NoteBlockContentResponseDTO {
    @ApiModelProperty(value="라벨", example = "New", required = true)
    private String label;

    @ApiModelProperty(value="블럭 컨텍스트 리스트", required = true)
    private List<BlockContextContentResponseDTO> contexts;
}
