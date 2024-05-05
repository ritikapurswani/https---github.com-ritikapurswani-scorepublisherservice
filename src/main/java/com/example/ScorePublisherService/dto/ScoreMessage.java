package com.example.ScorePublisherService.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ScoreMessage {
    private String playerName;
    private long score;
    private String userId;
}
