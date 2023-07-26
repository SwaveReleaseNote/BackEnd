package com.swave.urnr.releasenote.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.swave.urnr.releasenote.domain.ReleaseNote;
import com.swave.urnr.user.domain.UserInProject;

import static com.swave.urnr.project.domain.QProject.project;
import static com.swave.urnr.user.domain.QUserInProject.userInProject;
import static com.swave.urnr.releasenote.domain.QReleaseNote.releaseNote;

public class ReleaseNoteCustomRepositoryImpl implements ReleaseNoteCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public ReleaseNoteCustomRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public ReleaseNote findMostRecentReleaseNote(Long userId){

        return jpaQueryFactory
                .select(releaseNote)
                .from(userInProject)
                .join(project).on(userInProject.project.id.eq(project.id))
                .join(releaseNote).on(project.id.eq(releaseNote.project.id),
                        project.isDeleted.eq(false),
                        releaseNote.isDeleted.eq(false))
                .where(userInProject.user.id.eq(userId))
                .orderBy(releaseNote.lastModifiedDate.desc())
                .fetchFirst();

    }

    @Override
    public UserInProject findUserInProjectByUserIdAndReleaseNoteId(Long userId, Long releaseNote_id){
        return jpaQueryFactory
                .select(userInProject)
                .from(userInProject)
                .join(project).on(project.id.eq(userInProject.project.id))
                .join(releaseNote).on(releaseNote.project.id.eq(project.id))
                .where(releaseNote.id.eq(releaseNote_id),
                        userInProject.user.id.eq(userId),
                        project.isDeleted.eq(false),
                        releaseNote.isDeleted.eq(false))
                .fetchOne();
    }

    @Override
    public String latestReleseNote( Long projectId) {
        return jpaQueryFactory
                .select(releaseNote.version)
                .from(releaseNote)
                .where(releaseNote.project.id.eq(projectId))
                .orderBy(releaseNote.id.desc())
                .fetchFirst();
    }
}
