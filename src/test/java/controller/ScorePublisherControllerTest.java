package controller;

import com.example.ScorePublisherService.controller.ScorePublisherController;
import com.example.ScorePublisherService.dto.ScoreMessage;
import com.example.ScorePublisherService.producer.ScoreProducer;
import com.example.ScorePublisherService.util.ValidationUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ScorePublisherControllerTest {

    @Mock
    private ScoreProducer scoreProducer;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private ScorePublisherController controller;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testPublishScore_ValidInput_Success() throws JsonProcessingException {
        ScoreMessage validScoreMessage = new ScoreMessage("Player", 100,"34567");
        String validScoreMessageJson = "{\"playerName\":\"Player\",\"score\":100}";

        when(objectMapper.writeValueAsString(validScoreMessage)).thenReturn(validScoreMessageJson);
        ResponseEntity<String> response = controller.publishScore(validScoreMessage);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Score saved successfully", response.getBody());
        verify(scoreProducer, times(1)).sendMessage(validScoreMessageJson);
    }

    @Test
    public void testPublishScore_EmptyPlayerName_BadRequest() {
        ScoreMessage invalidScoreMessage = new ScoreMessage("", 100,"");

        ResponseEntity<String> response = controller.publishScore(invalidScoreMessage);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid player name or score value.", response.getBody());
        verify(scoreProducer, never()).sendMessage(anyString());
    }

    // Add more test cases for other scenarios
}

