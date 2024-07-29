package com.example.ewallet.user.configuration;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfiguration {

    /*
        Outside of scope of application
        1. Zookeeper
        2. bootstrap server
        3. created topic
    */

    /*
    * For Application Scope
    * 1. Configure a producer
    * 2. Configure a template via which my code can communicate with kafka
    * 3. Send kafka event.
    *
    * */


    public Map<String, Object> kafkaProducerConfigs() {
        Map<String, Object> configs = new HashMap<>();
        //configs.put("bootstrap.servers", "localhost:9092");
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        //configs.put("key.serializer", StringSerializer.class);
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        //configs.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return configs;
    }

    @Bean
    public ProducerFactory<String, String> producerFactory() {
       return new DefaultKafkaProducerFactory<>(kafkaProducerConfigs());
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
