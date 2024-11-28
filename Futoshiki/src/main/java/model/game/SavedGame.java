package model.game;

public class SavedGame {
    private GameState gameState;
    private String config;
    private long timestamp;

    /**
     * Constructor de la clase SavedGame.
     * 
     * @param gameState El estado del juego.
     * @param config La configuración del juego.
     * @param timestamp La marca de tiempo del guardado.
     */
    public SavedGame(GameState gameState, String config, long timestamp) {
        this.gameState = gameState;
        this.config = config;
        this.timestamp = timestamp;
    }

    /**
     * Obtiene el estado del juego.
     * 
     * @return El estado del juego.
     */
    public GameState getGameState() { return gameState; }

    /**
     * Obtiene la configuración del juego.
     * 
     * @return La configuración del juego.
     */
    public String getConfig() { return config; }

    /**
     * Obtiene la marca de tiempo del guardado.
     * 
     * @return La marca de tiempo del guardado.
     */
    public long getTimestamp() { return timestamp; }
} 