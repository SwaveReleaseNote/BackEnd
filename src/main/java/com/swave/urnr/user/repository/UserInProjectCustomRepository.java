package com.swave.urnr.user.repository;

public interface UserInProjectCustomRepository {

    Integer countMember(Long projectId);

    Integer deleteUser(Long projectId, Long deleteUserId);


}
