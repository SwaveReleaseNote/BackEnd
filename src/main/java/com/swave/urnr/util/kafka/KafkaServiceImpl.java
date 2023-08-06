package com.swave.urnr.util.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.DeleteTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;


@Service
@RequiredArgsConstructor
@Slf4j
@EnableTransactionManagement
public class KafkaServiceImpl implements  KafkaService{

    /*
    To prevent error before implementation to cloud, temporary  disable Kafka service.
    It will disappear after fully imported to cloud server during our project, so everything is ok for now.
     */


    @Override
    public String createTopic(String topicName) {
        return "This is test message.";
    }
    @Override
    public void produceMessage(NotificationDTO notificationDTO, String topic) {
    }
















//    @Override
//    public String createTopic(String topicName) {
//        try {
//            Properties properties = new Properties();
//            properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
//
//            try (AdminClient adminClient = AdminClient.create(properties)) {
//                NewTopic newTopic = new NewTopic(topicName, 1, (short) 1);
//                adminClient.createTopics(Collections.singleton(newTopic));
//
//                String message = "start";
//                KafkaTemplate<String, String> temp = new KafkaTemplate<>(producerFactory());
//                temp.send(topicName, message);
//
//                return "Topic created successfully: " + topicName;
//            }
//        } catch (Exception e) {
//            return "Failed to create topic: " + e.getMessage();
//        }
//    }
//
//
//
//
//
//    @Override
//    public void produceMessage(NotificationDTO notificationDTO, String topic) {
//        KafkaTemplate<String, String> kafkaTemplate = new KafkaTemplate<>(producerFactory());
//        ObjectMapper objectMapper = new ObjectMapper();
//        log.info(notificationDTO.getContent().toString());
//        log.info(String.valueOf(notificationDTO.getId()));
//        log.info(notificationDTO.getDate().toString());
//        log.info(notificationDTO.getType().toString());
//
//        try {
//            String jsonMessage = objectMapper.writeValueAsString(notificationDTO);
//            kafkaTemplate.send(topic, jsonMessage);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//    private ProducerFactory<String, String> producerFactory() {
//        Map<String, Object> configs = new HashMap<>();
//
//        configs.put("bootstrap.servers", "localhost:9092");
//        configs.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
//        configs.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
//
//        return new DefaultKafkaProducerFactory<>(configs);
//    }
//


}
