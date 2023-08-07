package com.swave.urnr.util.kafka;

import org.springframework.kafka.core.ProducerFactory;

import java.util.List;

public interface KafkaService {

    // Replace with the topic name you want to produce messages to


    void produceMessage(NotificationDTO notificationDTO , String topic) ;

    List<String> getMessagesFromKafkaTopic(String topic, int n);

    String createTopic(String topicName) ;


    List<String> getMessagesFromKafkaTopicFirst(String topic);
}
