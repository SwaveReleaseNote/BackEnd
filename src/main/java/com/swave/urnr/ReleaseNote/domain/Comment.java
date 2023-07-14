package com.swave.urnr.ReleaseNote.domain;

import com.swave.urnr.user.domain.User;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class Comment {
    @Id @Column(name = "comment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition="TEXT", name = "comment_context")
    private String commentContext;

    @Column(name = "last_modified_date")
    private Date lastModifiedDate;
  
    @ManyToOne
    @JoinColumn(name = "release_note_id")
    private ReleaseNote releaseNote;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    //todo : mention 필드 생성

    @Builder
    public Comment(String commentContext, Date lastModifiedDate, ReleaseNote releaseNote, User user) {
        this.commentContext = commentContext;
        this.lastModifiedDate = lastModifiedDate;

        //다른 entity와의 연결
        this.releaseNote = releaseNote;
        this.user = user;
    }
}
