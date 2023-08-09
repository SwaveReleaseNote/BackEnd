package com.swave.urnr.releasenote.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import static com.swave.urnr.releasenote.domain.QLiked.liked;
import static com.swave.urnr.releasenote.domain.QReleaseNote.releaseNote;
import static com.swave.urnr.releasenote.domain.QSeenCheck.seenCheck;

public class LikedCustomRepositoryImpl implements LikedCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public LikedCustomRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public long countByLiked(Long releaseNoteId){
        return jpaQueryFactory
                .select(liked.count())
                .from(releaseNote)
                .join(seenCheck).on(seenCheck.releaseNote.id.eq(releaseNote.id))
                .join(liked).on(liked.seenCheck.id.eq(seenCheck.id))
                .where(seenCheck.isDeleted.eq(false), releaseNote.isDeleted.eq(false), releaseNote.id.eq(releaseNoteId), liked.isLiked.eq(true))
                .fetchFirst();
    }
}
