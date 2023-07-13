package com.swave.releasenotesharesystem.User.repository;

import java.util.List;

public interface UserInProjectCustomRepository {

    Integer countMember(Long projectId);

    Integer deleteUser(Long projectId, Long deleteUserId);


}
