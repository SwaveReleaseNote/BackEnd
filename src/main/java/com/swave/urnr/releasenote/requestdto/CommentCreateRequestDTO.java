package com.swave.urnr.releasenote.requestdto;

import io.swagger.annotations.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@ApiModel(value = "새 댓글 생성 DTO")
@NoArgsConstructor
public class CommentCreateRequestDTO {
    @ApiModelProperty(value = "댓글 내용", example = "Good ~ !", required = true)
    private String content;
}
