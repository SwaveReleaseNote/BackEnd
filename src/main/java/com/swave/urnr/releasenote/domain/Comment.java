package com.swave.urnr.releasenote.domain;

import com.swave.urnr.releasenote.responsedto.CommentContentResponseDTO;
import com.swave.urnr.user.domain.User;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Where(clause = "is_deleted = false")
@SQLDelete(sql = "UPDATE comment SET is_deleted = true WHERE comment_id = ?")
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

    @Column(name = "is_deleted")
    private boolean isDeleted = Boolean.FALSE;

    //todo : mention 필드 생성

    @Builder
    public Comment(String commentContext, Date lastModifiedDate, ReleaseNote releaseNote, User user) {
        this.commentContext = commentContext;
        this.lastModifiedDate = lastModifiedDate;

        //다른 entity와의 연결
        this.releaseNote = releaseNote;
        this.user = user;
    }

    public CommentContentResponseDTO makeCommentContentResponseDTO(){
        CommentContentResponseDTO commentContentResponseDTO = new CommentContentResponseDTO();
        commentContentResponseDTO.setName(this.getUser().getUsername());
        commentContentResponseDTO.setContext(this.getCommentContext());
        commentContentResponseDTO.setLastModifiedDate(this.getLastModifiedDate());
        commentContentResponseDTO.setVersion(this.releaseNote.getVersion());
        commentContentResponseDTO.setReleaseNoteId(this.releaseNote.getId());

        return commentContentResponseDTO;
    }
}

