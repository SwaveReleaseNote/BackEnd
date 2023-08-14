package com.swave.urnr.util.kafka;

import java.util.List;

public interface KafkaService {

    // Replace with the topic name you want to produce messages to


    void produceMessage(NotificationDTO notificationDTO , String topic) ;

    KafkaMessageListDTO getMessageListFromKafka(String topic);

    List<String> getMessagesFromKafkaTopic(String topic, int n);

    String createTopic(String topicName) ;


    Boolean getCountFromSpecificTopic(String topic);

    List<String> getNewMessagesFromKafkaTopic(String topic);
}
