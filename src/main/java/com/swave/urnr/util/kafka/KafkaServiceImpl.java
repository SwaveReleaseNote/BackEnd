package com.swave.urnr.util.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swave.urnr.user.repository.UserRepository;
import com.swave.urnr.util.sse.SSEDataDTO;
import com.swave.urnr.util.sse.SSEEmitterService;
import com.swave.urnr.util.sse.SSETypeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.*;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


@Service
@RequiredArgsConstructor
@Slf4j
@EnableTransactionManagement
public class KafkaServiceImpl implements  KafkaService{

    private final UserRepository userRepository;
    private final SSEEmitterService sseEmitterService;

    private ObjectMapper objectMapper = new ObjectMapper();

    private static final Map<String, AnnotationConfigApplicationContext> LISTENER = new ConcurrentHashMap<>();



    @Value("${spring.kafka.bootstrap-servers}")
    private String hostLocation ;
    @Override
    public String createTopic(String topicName) {
        try {
            Properties properties = new Properties();
            properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, hostLocation);

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
    public Boolean isPresentTopic(String topicName){
        Properties properties = new Properties();
        properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

        try (AdminClient adminClient = AdminClient.create(properties)) {
            ListTopicsOptions options = new ListTopicsOptions();
            options.listInternal(true);
            ListTopicsResult topicsResult = adminClient.listTopics(options);
            Set<String> topicNames = topicsResult.names().get();

            return topicNames.contains(topicName);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }




    @Override
    public void produceMessage(NotificationDTO notificationDTO, String topic) {
        KafkaTemplate<String, String> kafkaTemplate = new KafkaTemplate<>(producerFactory());
        ObjectMapper objectMapper = new ObjectMapper();
//        log.info(notificationDTO.getContent().toString());
//        log.info(String.valueOf(notificationDTO.getReleaseNoteId()));
//        log.info(notificationDTO.getDate().toString());
//        log.info(notificationDTO.getType().toString());

        try {
            String jsonMessage = objectMapper.writeValueAsString(notificationDTO);
            kafkaTemplate.send(topic, jsonMessage);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void produceMessageAsString(String message, String topic){
        KafkaTemplate<String, String> kafkaTemplate = new KafkaTemplate<>(producerFactory());
            kafkaTemplate.send(topic, message);
    }

    @Override
    public KafkaMessageListDTO getMessageListFromKafka(String topic){
        List<String> newMessage = getNewMessagesFromKafkaTopic(topic);
        List<String> oldMessage = getMessagesFromKafkaTopic(topic, 10);
        KafkaMessageListDTO kafkaMessageListDTO = new KafkaMessageListDTO(oldMessage, newMessage);
        return kafkaMessageListDTO;
    }

    @Override
    public List<String> getMessagesFromKafkaTopic(String topic, int n) {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", hostLocation);
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("group.id", topic+ System.currentTimeMillis());
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
        messages.remove("start");

        userRepository.findById(Long.valueOf(topic)).orElseThrow(NoSuchElementException::new).setKafka_count(Long.valueOf(messages.size()));
        userRepository.flush();
        if( n < messages.size()){



            int messageSize = messages.size();
            for(int i=0;i<messageSize-n;i++){
                messages.remove(0);
            }

        }
        kafkaConsumer.close();

        return messages;
    }

    @Override
    public Boolean getCountFromSpecificTopic(String topic) {
        Properties properties = new Properties();

        properties.put("bootstrap.servers", hostLocation);
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("group.id", topic+ System.currentTimeMillis());
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
        log.info("BM : {}", messages.size());
        messages.remove("start");
        log.info("AM : {}", messages.size());
        Long kafkaValue = userRepository.findById(Long.valueOf(topic)).orElseThrow(NoSuchElementException::new).getKafka_count();
        log.info(" kaf val : {} , ms val : {} ,   Ex Val :{} ",kafkaValue , messages.size(),kafkaValue == messages.size());
        return kafkaValue == messages.size();

    }

    @Override
    public List<String> getNewMessagesFromKafkaTopic(String topic) {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", hostLocation);
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

        configs.put("bootstrap.servers", hostLocation);
        configs.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        configs.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        return new DefaultKafkaProducerFactory<>(configs);
    }

    @Override
    @KafkaListener(topics = "emitter", groupId = "urnremitter")
    public void sentToEmitter(String message) {


        log.info(" YOUGOT : {} on emitter", message);
        Long currentTime = System.currentTimeMillis();
        Date currentDate = new Date(currentTime);
        SSEDataDTO sseDataDTO = new SSEDataDTO(message, SSETypeEnum.ALARM);

        try{

            Map<String, String> jsonMap = objectMapper.readValue(message, new TypeReference<Map<String, String>>() {});

            String userId = jsonMap.get("userId");
            String content = jsonMap.get("content");
            String type = jsonMap.get("type");

            System.out.println("We got :" + userId+ " "+ content + " "+type);

            System.out.println("Received message On Emitter (Should sent) : " + message);
            sseEmitterService.publishAlarmToEmitter(userId,content, SSETypeEnum.fromString(type));

        }catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);

        }

    }


}
