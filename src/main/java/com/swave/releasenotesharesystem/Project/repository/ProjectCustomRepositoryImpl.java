package com.swave.releasenotesharesystem.Project.repository;


import com.querydsl.jpa.impl.JPAQueryFactory;
import com.swave.releasenotesharesystem.Util.type.UserRole;

import static com.swave.releasenotesharesystem.User.domain.QUserInProject.userInProject;


import javax.persistence.EntityManager;
import java.util.List;

public class ProjectCustomRepositoryImpl implements ProjectCustomRepository {

    private final JPAQueryFactory queryFactory;

    public ProjectCustomRepositoryImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }



}
