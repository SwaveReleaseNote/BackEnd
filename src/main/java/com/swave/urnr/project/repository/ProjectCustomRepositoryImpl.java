package com.swave.urnr.project.repository;


import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.swave.urnr.project.responsedto.ProjectSearchListResponseDTO;

import com.swave.urnr.user.domain.QUserInProject;
import com.swave.urnr.user.responsedto.UserMemberInfoResponseDTO;
import com.swave.urnr.util.type.UserRole;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.types.Projections.list;
import static com.querydsl.core.types.dsl.Expressions.set;
import static com.swave.urnr.user.domain.QUser.user;
import static com.swave.urnr.user.domain.QUserInProject.userInProject;

import javax.persistence.EntityManager;
import java.util.List;

import static com.swave.urnr.project.domain.QProject.project;

public class ProjectCustomRepositoryImpl implements ProjectCustomRepository {

    private final JPAQueryFactory queryFactory;

    public ProjectCustomRepositoryImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

    //유저 삭제조건 추가
    @Override
    public List<ProjectSearchListResponseDTO> searchProjectByName(String keyword) {

        StringExpression projectFields = project.name;
        BooleanExpression keywordExpression = projectFields.contains(keyword);

        return queryFactory
                .select(Projections.constructor(ProjectSearchListResponseDTO.class,
                        project.id,
                        project.name,
                        project.description,
                        project.createDate,
                        user.id,
                        user.username,
                        user.department,
                        userInProject.role))
                .from(project)
                .join(userInProject).on(project.id.eq(userInProject.project.id))
                .join(user).on(userInProject.user.id.eq(user.id))
                .where(keywordExpression)
                .fetch();
    }

    @Override
    public List<ProjectSearchListResponseDTO> searchProjectByDescription(String keyword) {

        StringExpression projectFields = project.description;
        BooleanExpression keywordExpression = projectFields.contains(keyword);


        return queryFactory
                .select(Projections.constructor(ProjectSearchListResponseDTO.class,
                        project.id,
                        project.name,
                        project.description,
                        project.createDate,
                        user.id,
                        user.username,
                        user.department,
                        userInProject.role))
                .from(project)
                .join(userInProject).on(project.id.eq(userInProject.project.id))
                .join(user).on(userInProject.user.id.eq(user.id))
                .where(keywordExpression)
                .fetch();
    }

    @Override
    public List<ProjectSearchListResponseDTO> searchProjectByManager(String keyword) {

        StringExpression projectFields = user.username;
        BooleanExpression keywordExpression = projectFields.contains(keyword);

        List<Long> projectIdsWithRole = queryFactory
                .select(project.id)
                .from(project)
                .join(userInProject).on(project.id.eq(userInProject.project.id))
                .join(user).on(userInProject.user.id.eq(user.id))
                .where(keywordExpression,userInProject.role.eq(UserRole.Manager))
                .fetch();

        return queryFactory
                .select(Projections.constructor(ProjectSearchListResponseDTO.class,
                        project.id,
                        project.name,
                        project.description,
                        project.createDate,
                        user.id,
                        user.username,
                        user.department,
                        userInProject.role))
                .from(project)
                .join(userInProject).on(project.id.eq(userInProject.project.id))
                .join(user).on(userInProject.user.id.eq(user.id))
                .where(project.id.in(projectIdsWithRole))
                .fetch();
    }

    @Override
    public List<ProjectSearchListResponseDTO> searchProjectByDeveloper(String keyword) {

        StringExpression projectFields = user.username;
        BooleanExpression keywordExpression = projectFields.contains(keyword);

        List<Long> projectIdsWithRole = queryFactory
                .select(project.id)
                .from(project)
                .join(userInProject).on(project.id.eq(userInProject.project.id))
                .join(user).on(userInProject.user.id.eq(user.id))
                .where(keywordExpression,userInProject.role.eq(UserRole.Developer))
                .fetch();

        return queryFactory
                .select(Projections.constructor(ProjectSearchListResponseDTO.class,
                        project.id,
                        project.name,
                        project.description,
                        project.createDate,
                        user.id,
                        user.username,
                        user.department,
                        userInProject.role))
                .from(project)
                .join(userInProject).on(project.id.eq(userInProject.project.id))
                .join(user).on(userInProject.user.id.eq(user.id))
                .where(project.id.in(projectIdsWithRole))
                .fetch();
    }


}
