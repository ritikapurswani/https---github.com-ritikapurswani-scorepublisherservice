package com.example.ScorePublisherService.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ScoreMessage {
    private String playerName;
    private long score;
    private String userId;

    public ScoreMessage(String playerName, long score, String userId) {
        this.playerName = playerName;
        this.score = score;
        this.userId = userId;
    }
}
