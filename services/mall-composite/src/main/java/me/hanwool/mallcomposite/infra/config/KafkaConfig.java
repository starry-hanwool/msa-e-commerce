package me.hanwool.mallcomposite.infra.config;

import me.hanwool.mallutilapp.dto.OrderDTO;
import me.hanwool.mallutilapp.event.Event;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConfig {

//    @Value("kafka.output.orders.topic")
//    @Value("${spring.cloud.stream.bindings.output-orders.destination}")
//    public String OUTPUT_ORDERS;

//    @Value("kafka.output.coupon.topic")
//    @Value("spring.cloud.stream.bindings.output-coupons.destination")
//    private String OUTPUT_COUPONS;

    @Value("${kafka.input.topic}")
//    @Value("${spring.cloud.stream.bindings.input-composite.destination}")
    private String INPUT_COMPOSITE;

    @Value("${spring.kafka.consumer.group-id}")
//    @Value("${kafka.consumer.group.id}")
//    @Value("${spring.cloud.stream.bindings.input-composite.group}")
    private String GROUP_ID;

    @Value("${spring.kafka.bootstrap-servers}")
    private String BOOTSTRAP_SERVERS;

    @Bean
    ProducerFactory<Long, Event> producerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
//        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
//        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, io.confluent.kafka.serializers.KafkaAvroSerializer.class);
        return new DefaultKafkaProducerFactory<Long, Event>(config);
    }

    @Bean
    public ReplyingKafkaTemplate<Long, Event, Event> replyingKafkaTemplate(ProducerFactory<Long, Event> pf,
                                                                               ConcurrentKafkaListenerContainerFactory<Long, Event> factory) {
        ConcurrentMessageListenerContainer<Long, Event> replyContainer = factory.createContainer(INPUT_COMPOSITE);
        replyContainer.getContainerProperties().setMissingTopicsFatal(false);
        replyContainer.getContainerProperties().setGroupId(GROUP_ID);
        return new ReplyingKafkaTemplate<>(pf, replyContainer);
    }

    @Bean
    public KafkaTemplate<Long, Event> replyTemplate(ProducerFactory<Long, Event> pf,
                                                       ConcurrentKafkaListenerContainerFactory<Long, Event> factory) {
        KafkaTemplate<Long, Event> kafkaTemplate = new KafkaTemplate<Long, Event>(pf);
        factory.getContainerProperties().setMissingTopicsFatal(false);
        factory.setReplyTemplate(kafkaTemplate);
        return kafkaTemplate;
    }

    //--
    @Bean
    public ConsumerFactory<Long, Event> consumerFatory() {
//        JsonDeserializer deserializer = new JsonDeserializer();
        JsonDeserializer<Event> deserializer = new JsonDeserializer<>(Event.class);
        deserializer.addTrustedPackages("*");
//        deserializer.addTrustedPackages("me.hanwool.mallutilapp.event.Event");

        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
//        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
//        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class);
//        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
//        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
        config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        config.put(ConsumerConfig.FETCH_MAX_BYTES_CONFIG, "41943040");
//        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer);
//        config.put(JsonDeserializer.TRUSTED_PACKAGES, "me.hanwool.mallutilapp.event.Event");
//        return new DefaultKafkaConsumerFactory<>(config);
        return new DefaultKafkaConsumerFactory<Long, Event>(config, new LongDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<Long, Event> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<Long, Event> concurrentKafkaListenerContainerFactory = new ConcurrentKafkaListenerContainerFactory<>();
        concurrentKafkaListenerContainerFactory.setConsumerFactory(consumerFatory());
        concurrentKafkaListenerContainerFactory.setMissingTopicsFatal(false);
        return concurrentKafkaListenerContainerFactory;
    }

}
