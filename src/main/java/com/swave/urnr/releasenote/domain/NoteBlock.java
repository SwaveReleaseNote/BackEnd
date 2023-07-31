package com.swave.urnr.releasenote.domain;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Where(clause = "is_deleted = false")
@SQLDelete(sql = "UPDATE note_block SET is_deleted = true WHERE note_block_id = ?")
@NoArgsConstructor
public class NoteBlock {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "note_block_id")
    private Long id;

    @Column(name = "label")
    private String label;

    @OneToMany(mappedBy = "noteBlock", orphanRemoval = true )
    @Column(name = "block_context_id")
    private List<BlockContext> blockContextList;

    @ManyToOne
    @JoinColumn(name = "release_note_id")
    private ReleaseNote releaseNote;

    @Column(name = "is_deleted")
    private boolean isDeleted = Boolean.FALSE;

    @Builder
    public NoteBlock(String label, List<BlockContext> blockContextList , ReleaseNote releaseNote) {
        this.label = label;

        //다른 entity와의 연결
        this.blockContextList = blockContextList;
        this.releaseNote = releaseNote;
    }
}
