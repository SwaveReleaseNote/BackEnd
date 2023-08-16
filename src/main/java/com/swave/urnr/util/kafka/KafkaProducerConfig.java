package com.swave.urnr.util.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    private static final String KAFKA_BROKER_ADDRESS = "localhost:9092";

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_BROKER_ADDRESS);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public NewTopic emitter() {


            /*
            TODO(ASAP) : List형태로 모두 받는지 확인할것, 또한, List 이전 작업 역시 확인할것.
            kafka 통신 사례  :
            1. 알림 시스템 ( 릴리즈노트 생성 직전 프로젝트 전 이해관계자들에게 / 작성자가 아닌 댓글 달릴시 작성자에게
            /  멘션 관련자에게.  즉 총3가지)
            -> Topic 메세지 발행하면서 2개 채널에 메세지 발행(1. emitter / 2. userId)
            emitter는 간단하게 userId만
            userId는 풀로
            ->  메세지 받은 곳 중
             emitter에는 userId에 맞는 emitter에 api 유도 알림 보냄
             userId에는 userId에 맞도록 ( 타입 | 날짜 | 알림내용 | 릴리즈노트 Id ) 4가지를 넣어줌(NotificationDTO)
             이를 통해 클라이언트는 알림 내용을 요구하는 api(all)를 요구가능.
            ->  받은 client는 종에 알림 생성(붉은 동그라미) / 유저는 종 누르면 api 눌러서 알림리스트 받아옴
            ->  진행...

             */
        return TopicBuilder.name("emitter")
                .partitions(3)
                .replicas(3)
                .build();
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
