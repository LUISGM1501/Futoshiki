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
    public int getGridSize() {
        return gridSize;
    }

    public void setGridSize(int gridSize) {
        this.gridSize = gridSize;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public boolean isMultiLevel() {
        return isMultiLevel;
    }

    public void setMultiLevel(boolean isMultiLevel) {
        this.isMultiLevel = isMultiLevel;
    }

    public String getTimerType() {
        return timerType;
    }

    public void setTimerType(String timerType) {
        this.timerType = timerType;
    }

    public int getTimerHours() {
        return timerHours;
    }

    public void setTimerHours(int timerHours) {
        this.timerHours = timerHours;
    }

    public int getTimerMinutes() {
        return timerMinutes;
    }

    public void setTimerMinutes(int timerMinutes) {
        this.timerMinutes = timerMinutes;
    }

    public int getTimerSeconds() {
        return timerSeconds;
    }

    public void setTimerSeconds(int timerSeconds) {
        this.timerSeconds = timerSeconds;
    }

    public String getDigitPanelPosition() {
        return digitPanelPosition;
    }

    public void setDigitPanelPosition(String digitPanelPosition) {
        this.digitPanelPosition = digitPanelPosition;
    }

    public String getPlayerName() {
        return playerName;
    }
    
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}