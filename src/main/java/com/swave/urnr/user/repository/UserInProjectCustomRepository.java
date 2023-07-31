package com.swave.urnr.user.repository;

import com.swave.urnr.user.domain.User;
import com.swave.urnr.user.responsedto.UserMemberInfoResponseDTO;

import java.util.List;

public interface UserInProjectCustomRepository {

    Integer countMember(Long projectId);

    Integer deleteUser( Long deleteUserId,Long projectId);

    List<UserMemberInfoResponseDTO> getMembers(Long projectId);

    Integer dropProject(Long userId, Long projectId);

    List<UserMemberInfoResponseDTO> getLoginMembers(Long projectId);


}
