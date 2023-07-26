 package com.swave.urnr.util.common;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.*;

 @ApiModel(value = "응답 전송용 DTO")
@AllArgsConstructor
@Getter
public class ResponseDTO<T> {

    @ApiModelProperty(value="HTTP 상태 코드", example = "404", required = true)
    @ApiParam(value = "상태 코드", required = true, example = "상태 코드를 입력하세요")
    private int status;
    @ApiModelProperty(value="전송 데이터", example = "artisheep@naver.com", required = true)
    @ApiParam(value = "전송 데이터", required = true, example = "전송할 데이터를 입력하세요.")
    private T data;

}