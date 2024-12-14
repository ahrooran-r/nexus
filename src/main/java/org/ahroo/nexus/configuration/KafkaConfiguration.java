package org.ahroo.nexus.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.ahroo.nexus.dto.notification.Notification;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
@EnableKafka
@RequiredArgsConstructor
public class KafkaConfiguration {

    private final ObjectMapper jackson;

    @Bean
    public KafkaTemplate<String, Notification> notificationKafkaTemplate(KafkaProperties kafkaProperties) {
        ProducerFactory<String, Notification> producerFactory = new DefaultKafkaProducerFactory<>(kafkaProperties.buildProducerProperties(null), new StringSerializer(), new JsonSerializer<>(this.jackson));
        KafkaTemplate<String, Notification> kafkaTemplate = new KafkaTemplate<>(producerFactory);
        kafkaTemplate.setDefaultTopic(kafkaProperties.getTemplate().getDefaultTopic());
        return kafkaTemplate;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Notification> notificationKafkaListenerContainerFactory(KafkaProperties kafkaProperties) {
        ConcurrentKafkaListenerContainerFactory<String, Notification> factory = new ConcurrentKafkaListenerContainerFactory<>();
        ConsumerFactory<String, Notification> consumerFactory = new DefaultKafkaConsumerFactory<>(kafkaProperties.buildConsumerProperties(null), new StringDeserializer(), new JsonDeserializer<>(Notification.class, this.jackson));
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }
}
