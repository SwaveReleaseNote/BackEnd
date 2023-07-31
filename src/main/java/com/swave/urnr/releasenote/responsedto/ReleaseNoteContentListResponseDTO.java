package com.swave.urnr.releasenote.responsedto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.Date;

//작성자
//버전
//최종 수정 날짜(내가 생성)
//릴리즈 날짜(유저가)
//내용

@Data
@ApiModel(value = "릴리즈 노트 리스트 가져오기(요약본) DTO")
@NoArgsConstructor
public class ReleaseNoteContentListResponseDTO {

    @ApiModelProperty(value="글쓴이", example = "함건욱", required = true)
    private String creator;
    @ApiModelProperty(value="릴리즈 노트 버전", example = "1.0.1", required = true)
    private String version;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @ApiModelProperty(value="최종 수정 시", example = "2023-07-10", required = true)
    private Date lastModified;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @ApiModelProperty(value="릴리즈 날짜", example = "2023-07-11", required = true)
    private Date releaseDate;
    @ApiModelProperty(value="세 줄 요약", example = "lenovo를 상향했습니다.", required = true)
    private String summary;
}
