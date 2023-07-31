package com.swave.urnr.releasenote.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Data
@Entity
@Where(clause = "is_deleted = false")
@SQLDelete(sql = "UPDATE block_context SET is_deleted = true WHERE block_context_id = ?")
@NoArgsConstructor
public class BlockContext {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "block_context_id")
    private Long id;

    @Column(name = "tag")
    private String tag;

    @Column(name = "context")
    private String context;

    @Column(name = "indexing")
    private Long index;

    @ManyToOne
    @JoinColumn(name = "note_block_id")
    private NoteBlock noteBlock;

    @Column(name = "is_deleted")
    private boolean isDeleted = Boolean.FALSE;

    @Builder
    public BlockContext(Long index, NoteBlock noteBlock, String tag, String context) {
        this.index = index;
        this.tag = tag;
        this.context = context;

        this.noteBlock = noteBlock;
    }

}
