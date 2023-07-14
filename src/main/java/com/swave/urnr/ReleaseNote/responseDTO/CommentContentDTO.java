package com.swave.urnr.ReleaseNote.responseDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class CommentContentDTO {
    private String name;
    private String context;
    private Date lastModifiedDate;
}
