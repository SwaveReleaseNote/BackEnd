package com.swave.urnr.util.kafka;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    public void produceMessage(@RequestBody String message, @PathVariable String topic) {
       kafkaService.produceMessage(message,topic);
    }
    @PostMapping("/create-topic")
    public String createTopic(@RequestBody String topicName) {
        return kafkaService.createTopic(topicName);
    }

}
