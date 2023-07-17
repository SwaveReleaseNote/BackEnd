package com.swave.urnr.releasenote.domain;

import com.swave.urnr.project.domain.Project;
import com.swave.urnr.releasenote.responsedto.ReleaseNoteContentResponseDTO;
import com.swave.urnr.releasenote.responsedto.ReleaseNoteContentListResponseDTO;
import com.swave.urnr.user.domain.User;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Where(clause = "is_deleted = false")
@ToString(exclude = {"user", "project", "commentList", "noteBlockList"})
@SQLDelete(sql = "UPDATE comment SET is_deleted = true WHERE comment_id = ?")
@NoArgsConstructor
public class ReleaseNote {
    @Id @Column(name = "release_note_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "version")
    private String version;
    @Column(name = "last_modified_date")
    private Date lastModifiedDate;
    @Column(name = "release_date")
    private Date releaseDate;
    @Column(name = "count")
    private int count; // 조회수
    @Column(name = "is_updated")
    private boolean isUpdated = false;
    @Column(name = "summary")
    private String summary;

    // User 와 mapping
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    // Project 와 mapping
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    // Comment 와 mapping
    @OneToMany(mappedBy = "releaseNote", fetch = FetchType.LAZY, orphanRemoval = true )
    @Column(name = "comment_id")
    private List<Comment> commentList;

    // note block 과 mapping
    @OneToMany(mappedBy = "releaseNote", orphanRemoval = true )
    @Column(name = "note_block_id")
    private List<NoteBlock> noteBlockList;

    @Column(name = "is_deleted")
    private boolean isDeleted = Boolean.FALSE;

    @Builder
    public ReleaseNote(String version, Date lastModifiedDate, Date releaseDate, int count, Boolean isUpdated, String summary, User user, Project project, List<NoteBlock> noteBlockList, List<Comment> commentList) {
        this.version = version;
        this.lastModifiedDate = lastModifiedDate;
        this.releaseDate = releaseDate;
        this.count = count;
        this.isUpdated = isUpdated;
        this.summary = summary;

        //다른 entity와의 연결
        this.user = user;
        this.project = project;
        this.commentList = commentList;
        this.noteBlockList = noteBlockList;
    }

    public ReleaseNoteContentResponseDTO makeReleaseNoteContentDTO(){
        ReleaseNoteContentResponseDTO releaseNoteContentResponseDTO = new ReleaseNoteContentResponseDTO();
        ArrayList<String> noteBlockContentList = new ArrayList<>();

        for (NoteBlock noteblock:this.noteBlockList){
            noteBlockContentList.add(noteblock.getNoteBlockContext());
        }

        releaseNoteContentResponseDTO.setCreator(this.user.getUsername());
        releaseNoteContentResponseDTO.setVersion(this.version);
        releaseNoteContentResponseDTO.setLastModified(this.lastModifiedDate);
        releaseNoteContentResponseDTO.setReleaseDate(this.releaseDate);
        releaseNoteContentResponseDTO.setContent(noteBlockContentList.toString());
        releaseNoteContentResponseDTO.setCount(this.count);
        releaseNoteContentResponseDTO.setSummary(this.summary);
        //releaseNoteContentDTO.setLiked(this);

        return releaseNoteContentResponseDTO;
    }

    public ReleaseNoteContentListResponseDTO makeReleaseNoteContentListDTO(){
        ReleaseNoteContentListResponseDTO releaseNoteContentListResponseDTO = new ReleaseNoteContentListResponseDTO();

        releaseNoteContentListResponseDTO.setCreator(this.user.getUsername());
        releaseNoteContentListResponseDTO.setReleaseDate(this.releaseDate);
        releaseNoteContentListResponseDTO.setLastModified(this.lastModifiedDate);
        releaseNoteContentListResponseDTO.setVersion(this.version);
        releaseNoteContentListResponseDTO.setSummary(this.summary);

        return releaseNoteContentListResponseDTO;
    }
}
