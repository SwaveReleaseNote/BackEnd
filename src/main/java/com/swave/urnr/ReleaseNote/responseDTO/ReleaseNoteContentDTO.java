package com.swave.urnr.ReleaseNote.responseDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;

@Data
@NoArgsConstructor
public class ReleaseNoteContentDTO {

    private String creator;
    private String version;
    private Date lastModified;
    private Date releaseDate;
    private String summary;
    private String content;
    private ArrayList<CommentContentDTO> comment;
    private int count;
    private int liked;
}
