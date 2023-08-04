package com.swave.urnr.util.kafka;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class NotificationDTO {
    private NotificationEnum type;
    private Date date;
    private String content;

    private long id;

}
