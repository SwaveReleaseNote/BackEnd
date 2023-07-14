package com.swave.urnr.ReleaseNote.responseDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
public class CommentContentListDTO {
    private ArrayList<CommentContentDTO> comments;
}
