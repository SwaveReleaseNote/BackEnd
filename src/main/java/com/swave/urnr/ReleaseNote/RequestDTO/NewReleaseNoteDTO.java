package com.swave.urnr.ReleaseNote.RequestDTO;

import lombok.*;

import java.util.Date;

//프로젝트 id
//버전
//최종 수정 날짜(내가 생성)
//릴리즈 날짜(유저가)
//내용
@Data
@NoArgsConstructor
public class NewReleaseNoteDTO {

    private String version;
    private Date releaseDate;
    private String content;
}
