package com.swave.urnr.ReleaseNote.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
public class NoteBlock {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "note_block_id")
    private Long id;

    @Column(columnDefinition = "TEXT", name = "note_block_context")
    private String noteBlockContext;

    @ManyToOne
    @JoinColumn(name = "release_note_id")
    private ReleaseNote releaseNote;
    //todo : label 필드 생성

    @Builder
    public NoteBlock(String noteBlockContext, ReleaseNote releaseNote) {
        this.noteBlockContext = noteBlockContext;

        //다른 entity와의 연결
        this.releaseNote = releaseNote;
    }
}
