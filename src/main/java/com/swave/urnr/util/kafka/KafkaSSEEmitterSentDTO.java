package com.swave.urnr.util.kafka;
import com.swave.urnr.util.sse.SSETypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class KafkaSSEEmitterSentDTO {
    private String userId;
    private String content;
    private SSETypeEnum type;

}
