package com.swave.urnr.util.kafka;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class KafkaMessageListDTO {
    private List<String> oldMessage;
    private List<String> newMessage;
}
