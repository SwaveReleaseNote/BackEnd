package com.swave.urnr.util.kafka;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "UserController")
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

        kafkaService.produceMessage(notificationDTO,topic);

    }
    @PostMapping("/create-topic")
    public String createTopic(@RequestBody String topicName) {
        return kafkaService.createTopic(topicName);
    }

@GetMapping("/get-topic/{topicName}")
    public List<String> getMessageFromTopic(@RequestBody String list , @PathVariable String topicName){
        return kafkaService.getMessagesFromKafkaTopic(topicName, Integer.valueOf(list));

}


    @GetMapping("/get-topic-ue/{topicName}")
    public List<String> getMessageFromTopicFirst(  @PathVariable String topicName){
        return kafkaService.getMessagesFromKafkaTopicFirst(topicName);

    }
}