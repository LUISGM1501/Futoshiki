package model.game;

public class SavedGame {
    private GameState gameState;
    private String config;
    private long timestamp;

    public SavedGame(GameState gameState, String config, long timestamp) {
        this.gameState = gameState;
        this.config = config;
        this.timestamp = timestamp;
    }

    public GameState getGameState() { return gameState; }
    public String getConfig() { return config; }
    public long getTimestamp() { return timestamp; }
} 