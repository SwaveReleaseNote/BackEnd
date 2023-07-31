package com.swave.urnr.project.responsedto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectUserCheckDTO {
    Long memberId;
    String memberName;
    boolean isOnline;

}
