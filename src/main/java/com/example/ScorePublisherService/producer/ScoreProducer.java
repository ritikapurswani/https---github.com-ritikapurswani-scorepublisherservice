package com.example.ScorePublisherService.producer;

import com.example.ScorePublisherService.dto.ScoreMessage;
import org.springframework.messaging.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ScoreProducer {
    private static final Logger LOGGER = LogManager.getLogger(ScoreProducer.class);

    @Value("${kafka.topic:gaming-service-topic}")
    private String topicName;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String message) {

        kafkaTemplate.send(topicName, message);
        LOGGER.info("Message sent successfully to Kafka topic: {}", topicName);

    }
}