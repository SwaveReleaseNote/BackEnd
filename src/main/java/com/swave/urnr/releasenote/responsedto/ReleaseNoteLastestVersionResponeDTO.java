package com.swave.urnr.releasenote.responsedto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.catalina.authenticator.SavedRequest;
@Data
@ApiModel(value = "릴리즈 노트 가장 높은 버전 가져오기 DTO")
@NoArgsConstructor
public class ReleaseNoteLastestVersionResponeDTO {
    @ApiModelProperty(value="버전", example = "1.0.0", required = true)
    private String version;
}