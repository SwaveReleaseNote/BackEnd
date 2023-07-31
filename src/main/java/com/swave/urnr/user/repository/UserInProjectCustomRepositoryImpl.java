package com.swave.urnr.user.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.swave.urnr.user.domain.User;
import com.swave.urnr.user.responsedto.UserMemberInfoResponseDTO;
import com.swave.urnr.util.type.UserRole;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.stream.Collectors;

import static com.swave.urnr.user.domain.QUser.user;
import static com.swave.urnr.user.domain.QUserInProject.userInProject;
//import static com.swave.urnr.user.responsedto.QUserMemberInfoResponseDTO.userMemberInfoResponseDTO;

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
    public Integer deleteUser( Long deleteUserId, Long projectId) {
        return Math.toIntExact(queryFactory
                .delete(userInProject)
                .where(userInProject.project.id.eq(projectId).and(userInProject.user.id.eq(deleteUserId)))
                .execute());
    }


    @Override
    public List<UserMemberInfoResponseDTO> getMembers(Long projectId) {
        return queryFactory
                .select(Projections.constructor(UserMemberInfoResponseDTO.class, user.id, user.username, user.department))
                .from(userInProject)
                .join(user).on(userInProject.user.id.eq(user.id))
                .where(userInProject.project.id.eq(projectId),(userInProject.role.eq(UserRole.Developer)),(user.isDeleted.eq(false)))
                .fetch();
    }

    @Override
    public Integer dropProject(Long userId, Long projectId) {
        return Math.toIntExact(queryFactory.update(userInProject)
                .set(userInProject.isDeleted, true)
                .where(userInProject.project.id.eq(projectId).and(userInProject.user.id.eq(userId)))
                .execute());
    }

    @Override
    public List<UserMemberInfoResponseDTO> getLoginMembers(Long projectId){
        return queryFactory
                .select(Projections.constructor(UserMemberInfoResponseDTO.class, user.id, user.username, user.department))
                .from(userInProject)
                .join(user).on(userInProject.user.id.eq(user.user.id))
                .where(userInProject.project.id.eq(projectId))
                .fetch();
    }
}
