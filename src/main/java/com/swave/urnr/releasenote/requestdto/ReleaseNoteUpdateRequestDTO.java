package com.swave.urnr.releasenote.requestdto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@ApiModel(value = "릴리즈 노트 업데이트 DTO")
@NoArgsConstructor
public class ReleaseNoteUpdateRequestDTO {
    @ApiModelProperty(value="버전 명", example = "1.0.1", required = true)
    private String version;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @ApiModelProperty(value="릴리즈 날짜", example = "2023-07-08", required = true)
    private Date releaseDate;
    @ApiModelProperty(value="노트 블럭 리스트", required = true)
    private List<NoteBlockUpdateRequestDTO> blocks;
}
