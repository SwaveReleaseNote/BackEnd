package com.swave.urnr.ReleaseNote.RequestDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NewCommentDTO {
    private Long releaseNoteId;
    private String content;
}
