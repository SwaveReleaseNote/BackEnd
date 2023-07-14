package com.swave.urnr.ReleaseNote.RequestDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class UpdateReleaseNoteDTO {
    private Long releaseNoteId;
    private String version;
    private Date releaseDate;
    private String content;
}
