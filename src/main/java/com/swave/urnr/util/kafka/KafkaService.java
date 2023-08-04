package com.swave.urnr.util.kafka;

import org.springframework.kafka.core.ProducerFactory;

public interface KafkaService {

    // Replace with the topic name you want to produce messages to


    void produceMessage(NotificationDTO notificationDTO , String topic) ;
    String createTopic(String topicName) ;



}
