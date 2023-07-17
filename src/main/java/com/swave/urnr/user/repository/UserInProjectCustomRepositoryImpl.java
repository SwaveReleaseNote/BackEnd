package com.swave.urnr.user.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.swave.urnr.util.type.UserRole;

import javax.persistence.EntityManager;

import static com.swave.urnr.user.domain.QUserInProject.userInProject;

public class UserInProjectCustomRepositoryImpl implements UserInProjectCustomRepository {

    private final JPAQueryFactory queryFactory;

    public UserInProjectCustomRepositoryImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }


    @Override
    public Integer countMember(Long projectId) {
        return Math.toIntExact(queryFactory
                .select(userInProject.count())
                .from(userInProject)
                .where(userInProject.project.id.eq(projectId).and(userInProject.role.ne(UserRole.Subscriber)))
                .fetchOne());

    }

    @Override
    public Integer deleteUser(Long projectId, Long deleteUserId) {
        return Math.toIntExact(queryFactory
                .delete(userInProject)
                .where(userInProject.project.id.eq(projectId).and(userInProject.user.id.eq(deleteUserId)))
                .execute());
    }
}
