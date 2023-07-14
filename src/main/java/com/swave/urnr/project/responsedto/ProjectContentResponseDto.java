package com.swave.urnr.project.responsedto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectContentResponseDto {
    Long id;
    String name;
    String description;
    Date createDate;
    //todo releasenote 나중에 구현할것
    //todo usercount 나중에 구현할것


}
