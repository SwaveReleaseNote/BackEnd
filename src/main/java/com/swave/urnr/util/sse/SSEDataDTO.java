package com.swave.urnr.util.sse;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@ApiModel(value = "SSE 전송용  DTO")
@AllArgsConstructor
@Getter
public class SSEDataDTO {

    @ApiModelProperty(value="전송 데이터", example = "{   \"dataResult\": \"{\\\"managerId\\\":1,\\\"managerName\\\":\\\"강준희\\\",\\\"managerDepartment\\\":null,\\\"users\\\":[{\\\"userId\\\":1,\\\"username\\\":\\\"강준희\\\",\\\"userDepartment\\\":null},{\\\"userId\\\":2,\\\"username\\\":\\\"김기현\\\",\\\"userDepartment\\\":null},{\\\"userId\\\":3,\\\"username\\\":\\\"전강훈\\\",\\\"userDepartment\\\":null},{\\\"userId\\\":4,\\\"username\\\":\\\"함건욱\\\",\\\"userDepartment\\\":null},{\\\"userId\\\":5,\\\"username\\\":\\\"김성국\\\",\\\"userDepartment\\\":null},{\\\"userId\\\":6,\\\"username\\\":\\\"이승섭\\\",\\\"userDepartment\\\":null}]}\"\n}", required = true)
    private String data;

    @ApiModelProperty(value="타입", example = "token", required = true)
    private SSETypeEnum type;
}
