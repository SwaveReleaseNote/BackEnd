package com.swave.urnr.ReleaseNote.domain;

import com.swave.urnr.project.domain.Project;
import com.swave.urnr.ReleaseNote.responseDTO.ReleaseNoteContentDTO;
import com.swave.urnr.ReleaseNote.responseDTO.ReleaseNoteContentListDTO;
import com.swave.urnr.user.domain.User;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
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
    @OneToMany(mappedBy = "releaseNote", fetch = FetchType.LAZY)
    @Column(name = "comment_id")
    private List<Comment> commentList;

    // note block 과 mapping
    @OneToMany(mappedBy = "releaseNote")
    @Column(name = "note_block_id")
    private List<NoteBlock> noteBlockList;

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

    public ReleaseNoteContentDTO makeReleaseNoteContentDTO(){
        ReleaseNoteContentDTO releaseNoteContentDTO = new ReleaseNoteContentDTO();
        ArrayList<String> noteBlockContentList = new ArrayList<>();

        for (NoteBlock noteblock:this.noteBlockList){
            noteBlockContentList.add(noteblock.getNoteBlockContext());
        }

        releaseNoteContentDTO.setCreator(this.user.getName());
        releaseNoteContentDTO.setVersion(this.version);
        releaseNoteContentDTO.setLastModified(this.lastModifiedDate);
        releaseNoteContentDTO.setReleaseDate(this.releaseDate);
        releaseNoteContentDTO.setContent(noteBlockContentList.toString());
        releaseNoteContentDTO.setCount(this.count);
        releaseNoteContentDTO.setSummary(this.summary);
        //releaseNoteContentDTO.setLiked(this);

        return releaseNoteContentDTO;
    }

    public ReleaseNoteContentListDTO makeReleaseNoteContentListDTO(){
        ReleaseNoteContentListDTO releaseNoteContentListDTO = new ReleaseNoteContentListDTO();

        releaseNoteContentListDTO.setCreator(this.user.getName());
        releaseNoteContentListDTO.setReleaseDate(this.releaseDate);
        releaseNoteContentListDTO.setLastModified(this.lastModifiedDate);
        releaseNoteContentListDTO.setVersion(this.version);
        releaseNoteContentListDTO.setSummary(this.summary);

        return releaseNoteContentListDTO;
    }
}
