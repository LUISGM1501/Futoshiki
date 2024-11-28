package model.game;

import java.io.Serializable;

public class GameScore implements Comparable<GameScore>, Serializable {
    private static final long serialVersionUID = 1L;
    private String playerName;
    private int hours;
    private int minutes;
    private int seconds;
    private String difficulty;
    private int gridSize;

    public GameScore(String playerName, int hours, int minutes, int seconds, 
                    String difficulty, int gridSize) {
        this.playerName = playerName;
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
        this.difficulty = difficulty;
        this.gridSize = gridSize;
    }

    // Método para comparar puntuaciones
    @Override
    public int compareTo(GameScore other) {
        // Convertir todo a segundos para comparar
        int thisTotal = this.hours * 3600 + this.minutes * 60 + this.seconds;
        int otherTotal = other.hours * 3600 + other.minutes * 60 + other.seconds;
        return thisTotal - otherTotal;
    }

    // Getters y setters

    public String getPlayerName() {
        return playerName;
    }

    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public int getGridSize() {
        return gridSize;
    }
}