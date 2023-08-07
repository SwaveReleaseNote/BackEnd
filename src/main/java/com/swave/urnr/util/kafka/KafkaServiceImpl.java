package com.swave.urnr.util.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.time.Duration;
import java.util.*;


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
        try {
            Properties properties = new Properties();
            properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

            try (AdminClient adminClient = AdminClient.create(properties)) {
                NewTopic newTopic = new NewTopic(topicName, 1, (short) 1);
                adminClient.createTopics(Collections.singleton(newTopic));

                String message = "start";
                KafkaTemplate<String, String> temp = new KafkaTemplate<>(producerFactory());
                temp.send(topicName, message);

                return "Topic created successfully: " + topicName;
            }
        } catch (Exception e) {
            return "Failed to create topic: " + e.getMessage();
        }
    }





    @Override
    public void produceMessage(NotificationDTO notificationDTO, String topic) {
        KafkaTemplate<String, String> kafkaTemplate = new KafkaTemplate<>(producerFactory());
        ObjectMapper objectMapper = new ObjectMapper();
        log.info(notificationDTO.getContent().toString());
        log.info(String.valueOf(notificationDTO.getId()));
        log.info(notificationDTO.getDate().toString());
        log.info(notificationDTO.getType().toString());

        try {
            String jsonMessage = objectMapper.writeValueAsString(notificationDTO);
            kafkaTemplate.send(topic, jsonMessage);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<String> getMessagesFromKafkaTopic(String topic, int n) {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("group.id", topic+ System.currentTimeMillis());
        properties.put("auto.offset.reset", "earliest");



        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(properties);
        kafkaConsumer.subscribe(Collections.singletonList(topic));

        // Seek to the beginning of the topic
        kafkaConsumer.seekToBeginning(kafkaConsumer.assignment());

        List<String> messages = new ArrayList<>();
        int messagesToReceive = n;

        while (messagesToReceive > 0) {
            ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(1000));
            if (records.isEmpty()) {
                // No more messages to consume
                log.info("NO more message! ");
                break;
            }

            for (ConsumerRecord<String, String> record : records) {
                messages.add(record.value());
                messagesToReceive--;
                if (messagesToReceive == 0) {
                    log.info("NO more message to receive!! ");
                    break;
                }
            }
        }

        kafkaConsumer.close();
        messages.remove("start");
        return messages;
    }


    @Override
    public List<String> getMessagesFromKafkaTopicFirst(String topic) {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("group.id",topic);
        properties.put("auto.offset.reset", "earliest");


        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(properties);
        kafkaConsumer.subscribe(Collections.singletonList(topic));

        // Seek to the beginning of the topic
        kafkaConsumer.seekToBeginning(kafkaConsumer.assignment());

        List<String> messages = new ArrayList<>();
            ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(1000));

            for (ConsumerRecord<String, String> record : records) {
                messages.add(record.value());
            }

        kafkaConsumer.close();
            messages.remove("start");
        return messages;
    }

    private ProducerFactory<String, String> producerFactory() {
        Map<String, Object> configs = new HashMap<>();

        configs.put("bootstrap.servers", "localhost:9092");
        configs.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        configs.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        return new DefaultKafkaProducerFactory<>(configs);
    }



}
