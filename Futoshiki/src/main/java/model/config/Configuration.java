package model.config;

public class Configuration {
    private int gridSize;
    private String difficulty;
    private boolean isMultiLevel;
    private String timerType; // "chronometer", "none", "timer"
    private int timerHours;
    private int timerMinutes;
    private int timerSeconds;
    private String digitPanelPosition; // "right" or "left"
    private String playerName;

    public Configuration() {
        // Valores por defecto según especificación
        this.gridSize = 5;
        this.difficulty = "Facil";
        this.isMultiLevel = false;
        this.timerType = "chronometer";
        this.timerHours = 0;
        this.timerMinutes = 0;
        this.timerSeconds = 0;
        this.digitPanelPosition = "right";
        this.playerName = "";
    }

    // Getters y setters
}