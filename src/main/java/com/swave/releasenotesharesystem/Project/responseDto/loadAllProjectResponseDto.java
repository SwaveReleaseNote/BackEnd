package com.swave.releasenotesharesystem.Project.responseDto;

import com.swave.releasenotesharesystem.User.domain.UserInProject;
import com.swave.releasenotesharesystem.Util.type.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class loadAllProjectResponseDto {
    Long id;
    UserRole role;
    String name;
    String description;
    Date createDate;
    int count;
    //String version;
    //todo releasenote 나중에 구현할것
    //todo usercount 나중에 구현할것

}
