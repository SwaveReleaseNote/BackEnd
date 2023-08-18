package com.swave.urnr.util.kafka;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "Kafka Controller")
@RestController
@Validated
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/kafka")
@CrossOrigin
public class KafkaController {

    private final KafkaService kafkaService;



    @PostMapping("/{topic}")
    @Operation(summary="토픽 알림 메세지 생성", description="NotificationDTO에 맞는 사용자 정보를 topic에 저장합니다.")
    public void produceMessage(@RequestBody NotificationDTO notificationDTO, @PathVariable String topic) {

        kafkaService.produceMessage(notificationDTO, topic);

    }


    @PostMapping("/{topic}/message")
    @Operation(summary="토픽 메세지 전송", description="자유로운 string을 topic에 저장합니다. ")
    public void produceMessage(@RequestBody String message , @PathVariable String topic) {

        kafkaService.produceMessageAsString(message, topic);

    }

    @PostMapping("/create-topic")
    @Operation(summary="Topic 생성", description=" 입력받은 String에 맞는 Topic을 생성합니다.")
    public String createTopic(@RequestBody String topicName) {
        return kafkaService.createTopic(topicName);
    }

    @GetMapping("/get-topic/{topicName}/{num}")
    @Operation(summary="Topic 메세지 반환", description="Topic 받고 숫자만큼 최신 메세지 반환")
    public List<String> getMessageFromTopic(@RequestBody String list, @PathVariable String topicName) {
        return kafkaService.getMessagesFromKafkaTopic(topicName, Integer.valueOf(list));
    }

    @GetMapping("/get-topic-all/{topicName}")
    @Operation(summary="Topic 모든 메세지 반환", description="topic의 New 메세지와 모든 메세지를 반환합니다. ")
    public KafkaMessageListDTO getMessageFromTopicALL(@PathVariable String topicName) {
        return kafkaService.getMessageListFromKafka(topicName);
    }

    @GetMapping("/get-topic-new/{topicName}")
    @Operation(summary="Topic 메세지 반환", description="최신 topic 메세지를 받습니다.")
    public List<String> getMessageFromTopicNew(@PathVariable String topicName) {
        return kafkaService.getNewMessagesFromKafkaTopic(topicName);
    }

    @GetMapping("/get-topic-count/{topicName}")
    @Operation(summary="Topic 동기화 확인", description="Topic에 해당되는 유저의 알림이 최신화되었는지 확인합니다.")
    public Boolean isTopicLatest(@PathVariable String topicName) {
        return kafkaService.getCountFromSpecificTopic(topicName);

    }

}
