package com.swave.urnr.util.kafka;

import org.springframework.kafka.annotation.KafkaListener;

import java.util.List;

public interface KafkaService {

    // Replace with the topic name you want to produce messages to


    Boolean isPresentTopic(String topicName);


    void produceMessage(NotificationDTO notificationDTO , String topic) ;


    void produceMessageAsString(String message, String topic) ;
    void produceMessageS(String message, String topic) ;

    KafkaMessageListDTO getMessageListFromKafka(String topic);

    List<String> getMessagesFromKafkaTopic(String topic, int n);

    String createTopic(String topicName) ;


    Boolean getCountFromSpecificTopic(String topic);

    List<String> getNewMessagesFromKafkaTopic(String topic);

    @KafkaListener(topics = "emitter", groupId = "urnremitter")
    void sentToEmitter(String message);
}
