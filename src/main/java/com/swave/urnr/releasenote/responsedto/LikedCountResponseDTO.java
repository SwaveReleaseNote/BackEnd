package com.swave.urnr.releasenote.responsedto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@ApiModel(value = "좋아요 개수 카운트 DTO")
@NoArgsConstructor
public class LikedCountResponseDTO {
    @ApiModelProperty(value="좋아요 개수", example = "2", required = true)
    private Long likedCount;
}