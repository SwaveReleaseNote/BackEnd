package com.swave.urnr.releasenote.responsedto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@ApiModel(value = "릴리즈 노트 하나 가져오 DTO")
@NoArgsConstructor
public class ReleaseNoteContentResponseDTO {

    @ApiModelProperty(value="글쓴이", example = "전강훈", required = true)
    private String creator;
    @ApiModelProperty(value="릴리즈 노트 버전", example = "1.0.0", required = true)
    private String version;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @ApiModelProperty(value="최종 수정 시각", example = "2023-07-09", required = true)
    private Date lastModified;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @ApiModelProperty(value="릴리즈 날짜", example = "2023-07-08", required = true)
    private Date releaseDate;
    @ApiModelProperty(value="세 줄 요약", example = "DELL의 성능을 조정했습니다.", required = true)
    private String summary;
    @ApiModelProperty(value="노트 블럭 리스트", required = true)
    private List<NoteBlockContentResponseDTO> blocks;
    @ApiModelProperty(value="댓글 리스트", required = true)
    private List<CommentContentResponseDTO> comment;
    @ApiModelProperty(value="조회수", example = "3", required = true)
    private int count;
    @ApiModelProperty(value="좋아요 수", example = "-1", required = true)
    private int liked;
    @ApiModelProperty(value="릴리즈 노트 ID", example = "1", required = true)
    private Long releaseNoteId;
}
