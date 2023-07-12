package com.swave.releasenotesharesystem.Project.repository;


import com.querydsl.jpa.impl.JPAQueryFactory;
import static com.swave.releasenotesharesystem.User.domain.QUserInProject.userInProject;


import javax.persistence.EntityManager;

public class ProjectCustomRepositoryImpl implements ProjectCustomRepository {

    private final JPAQueryFactory queryFactory;

    public ProjectCustomRepositoryImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }


    @Override
    public Integer countMember(Long projectId) {
        return Math.toIntExact(queryFactory
                .select(userInProject.count())
                .from(userInProject)
                .where(userInProject.project.id.eq(projectId))
                .fetchOne());

    }
}
