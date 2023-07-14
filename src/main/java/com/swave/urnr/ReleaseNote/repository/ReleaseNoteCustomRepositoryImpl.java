package com.swave.urnr.ReleaseNote.repository;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;

import static com.swave.urnr.ReleaseNote.domain.QReleaseNote.releaseNote;


public class ReleaseNoteCustomRepositoryImpl implements ReleaseNoteCustomRepository{

    private final JPAQueryFactory queryFactory;

    public ReleaseNoteCustomRepositoryImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }


    @Override
    public String latestReleseNote(Long userId, Long projectId) {
        return queryFactory
                .select(releaseNote.version)
                .from(releaseNote)
                .where(releaseNote.user.id.eq(userId).and(releaseNote.project.id.eq(projectId)))
                .orderBy(releaseNote.id.desc())
                .fetchFirst();
    }
}
