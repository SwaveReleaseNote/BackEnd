package com.swave.urnr.util.kafka;

import io.swagger.annotations.Api;
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
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", allowCredentials = "true")
public class KafkaController {

    private final KafkaService kafkaService;


    @PostMapping("/produce/{topic}")
    public void produceMessage(@RequestBody NotificationDTO notificationDTO, @PathVariable String topic) {

        kafkaService.produceMessage(notificationDTO, topic);

    }


    @PostMapping("/produce/t/{topic}")
    public void produceMessage(@RequestBody String message , @PathVariable String topic) {

        kafkaService.produceMessageAsString(message, topic);

    }


    @PostMapping("/produce/te/{topic}")
    public void produceSampleMessage(@RequestBody String message , @PathVariable String topic) {

        kafkaService.produceMessageS(message, topic);

    }
    @PostMapping("/create-topic")
    public String createTopic(@RequestBody String topicName) {
        return kafkaService.createTopic(topicName);
    }

    @GetMapping("/get-topic/{topicName}")
    public List<String> getMessageFromTopic(@RequestBody String list, @PathVariable String topicName) {
        return kafkaService.getMessagesFromKafkaTopic(topicName, Integer.valueOf(list));

    }

    @GetMapping("/get-topic-all/{topicName}")

    public KafkaMessageListDTO getMessageFromTopicALL(@PathVariable String topicName) {
        return kafkaService.getMessageListFromKafka(topicName);

    }

    @GetMapping("/get-topic-new/{topicName}")
    public List<String> getMessageFromTopicNew(@PathVariable String topicName) {
        return kafkaService.getNewMessagesFromKafkaTopic(topicName);
    }

    @GetMapping("/get-topic-count/{topicName}")
    public Boolean getTopicCount(@PathVariable String topicName) {
        return kafkaService.getCountFromSpecificTopic(topicName);

    }

}
