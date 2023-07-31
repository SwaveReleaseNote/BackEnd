package com.swave.urnr.releasenote.responsedto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@ApiModel(value = "댓글 내용 DTO")
@NoArgsConstructor
public class CommentContentResponseDTO {
    @ApiModelProperty(value="댓글 작성자 이름", example = "김성국", required = true)
    private String name;
    @ApiModelProperty(value="댓글 내용", example = "ASUS도 너프 해야한다.", required = true)
    private String context;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @ApiModelProperty(value="최종 수정 시각", example = "2023-07-08", required = true)
    private Date lastModifiedDate;
    @ApiModelProperty(value="릴리즈 노트 버전", example = "1.0.0", required = true)
    private String version;
    @ApiModelProperty(value="릴리즈 노트 id", example = "25", required = true)
    private Long releaseNoteId;

}
