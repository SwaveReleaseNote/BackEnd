package com.swave.releasenotesharesystem.User.repository;

import com.swave.releasenotesharesystem.Util.type.UserRole;

import static com.swave.releasenotesharesystem.User.domain.QUserInProject.userInProject;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

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
