package com.swave.urnr.releasenote.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.swave.urnr.releasenote.responsedto.ReleaseNoteLabelContentResponseDTO;
import com.swave.urnr.releasenote.responsedto.ReleaseNoteLabelCountResponseDTO;

import java.util.List;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

import static com.swave.urnr.releasenote.domain.QReleaseNote.releaseNote;
import static com.swave.urnr.releasenote.domain.QNoteBlock.noteBlock;
import static com.swave.urnr.releasenote.domain.QBlockContext.blockContext;

public class NoteBlockCustomRepositoryImpl implements NoteBlockCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public NoteBlockCustomRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public List<ReleaseNoteLabelCountResponseDTO> countByLabel(Long projectId){
        return jpaQueryFactory
                .select(Projections.fields(ReleaseNoteLabelCountResponseDTO.class,
                        noteBlock.label,
                        noteBlock.count().as("count")))
                .from(noteBlock)
                .join(releaseNote).on(releaseNote.id.eq(noteBlock.releaseNote.id))
                .where(releaseNote.project.id.eq(projectId),
                        releaseNote.isDeleted.eq(false))
                .groupBy(noteBlock.label)
                .fetch();
    }

    @Override
    public List<ReleaseNoteLabelContentResponseDTO> filterByLabel(Long projectId){
        return jpaQueryFactory
                .selectFrom(releaseNote)
                .join(noteBlock).on(noteBlock.releaseNote.id.eq(releaseNote.id))
                .join(blockContext).on(blockContext.noteBlock.id.eq(noteBlock.id))
                .where(releaseNote.project.id.eq(projectId),
                        noteBlock.isDeleted.eq(false),
                        blockContext.isDeleted.eq(false))
                .transform(groupBy(noteBlock.label)
                        .list(
                                Projections.fields(ReleaseNoteLabelContentResponseDTO.class,
                                        noteBlock.label,
                                        releaseNote.id.as("releaseNoteId"),
                                        releaseNote.version,
                                        list(blockContext.context).as("context"))));
    }

}
