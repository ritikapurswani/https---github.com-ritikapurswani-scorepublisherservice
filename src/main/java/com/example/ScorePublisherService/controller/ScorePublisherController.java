package com.example.ScorePublisherService.controller;

import com.example.ScorePublisherService.dto.ScoreMessage;
import com.example.ScorePublisherService.producer.ScoreProducer;
import com.example.ScorePublisherService.util.ValidationUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.SerializationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/score")
public class ScorePublisherController {

    private static final Logger LOGGER = LogManager.getLogger(ScorePublisherController.class);
    @Autowired
    private ScoreProducer scoreProducer;
    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping("/publish")
    public ResponseEntity<String> publishScore(@RequestBody ScoreMessage scoreMessage) {
        try {
            LOGGER.info("Received score message: {}", scoreMessage);
            if (ValidationUtil.isNullOrEmpty(scoreMessage.getPlayerName()) ||
                    !ValidationUtil.isPositiveInteger(scoreMessage.getScore())
                    || ValidationUtil.isNullOrEmpty(scoreMessage.getUserId())) {
                return ResponseEntity.badRequest().body("Invalid player name, score value or user id");
            }

            String message = objectMapper.writeValueAsString(scoreMessage);

            scoreProducer.sendMessage(message);

            return ResponseEntity.ok("Score saved successfully");
        } catch (IllegalArgumentException e) {
            LOGGER.error("Validation failed: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (SerializationException e) {
            LOGGER.error("Error occurred during serialization: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to serialize message for publishing to Kafka topic.");
        } catch (Exception e) {
            LOGGER.error("Error occurred in publishing score to Kafka topic: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to save score: " + e.getMessage());
        }
    }
}
