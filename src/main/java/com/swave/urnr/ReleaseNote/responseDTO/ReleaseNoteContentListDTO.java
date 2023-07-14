package com.swave.urnr.ReleaseNote.responseDTO;

import lombok.*;

import java.util.Date;

//작성자
//버전
//최종 수정 날짜(내가 생성)
//릴리즈 날짜(유저가)
//내용

@Data
@NoArgsConstructor
public class ReleaseNoteContentListDTO {

    private String creator;
    private String version;
    private Date lastModified;
    private Date releaseDate;
    private String summary;
}
