package com.swave.urnr.util.kafka;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;


@ApiModel(value = "알림 전송용 DTO")
@AllArgsConstructor
@Getter
public class NotificationDTO {
    @ApiModelProperty(value="알림 type", example = "POST", required = true)
    @ApiParam(value = "알림 type", required = true, example = "POST 등 Notification의 enum이 필요합니다. POST / MENTION / COMMENT의 3가지가 있습니다.")
    private NotificationEnum type;

    @ApiModelProperty(value="알림 type", example = "POST", required = true)
    @ApiParam(value = "날짜", required = true, example = "POST 등 Notification의 enum이 필요합니다. POST / MENTION / COMMENT의 3가지가 있습니다.")
    private Date date;
    private String content;

    private long releaseNoteId;

}
