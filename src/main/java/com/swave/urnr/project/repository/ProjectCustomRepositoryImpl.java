package com.swave.urnr.project.repository;


import com.querydsl.jpa.impl.JPAQueryFactory;


import javax.persistence.EntityManager;

public class ProjectCustomRepositoryImpl implements ProjectCustomRepository {

    private final JPAQueryFactory queryFactory;

    public ProjectCustomRepositoryImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }



}
