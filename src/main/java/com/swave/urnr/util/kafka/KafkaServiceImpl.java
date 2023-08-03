package com.swave.urnr.util.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
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


@Service
@RequiredArgsConstructor
@Slf4j
@EnableTransactionManagement
public class KafkaServiceImpl implements  KafkaService{

    // Replace with the topic name you want to produce messages to

    @Override
    public void produceMessage(String message,  String topic) {
        KafkaTemplate<String, String> temp = new KafkaTemplate<>(producerFactory());
        temp.send(topic, message);
    }

    @Override
    public String createTopic(String topicName) {
        try {
            Properties properties = new Properties();
            properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

            try (AdminClient adminClient = AdminClient.create(properties)) {
                NewTopic newTopic = new NewTopic(topicName, 1, (short) 1);
                adminClient.createTopics(Collections.singleton(newTopic));

                // Send a temporary message to the newly created topic for verification
                String message = "Hello, this is a temporary message to check the created topic!";
                KafkaTemplate<String, String> temp = new KafkaTemplate<>(producerFactory());
                temp.send(topicName, message);

                return "Topic created successfully: " + topicName;
            }
        } catch (Exception e) {
            return "Failed to create topic: " + e.getMessage();
        }
    }

    private ProducerFactory<String, String> producerFactory() {
        Map<String, Object> configs = new HashMap<>();

        configs.put("bootstrap.servers", "localhost:9092");
        configs.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        configs.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        return new DefaultKafkaProducerFactory<>(configs);
    }
}
