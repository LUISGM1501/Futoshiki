package persistence;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import model.config.Configuration;
import model.game.GameState;
import util.constants.FileConstants;

public class GameSaver {
    
    // Guarda el estado actual del juego
    // @return true si se guardó correctamente
    public static boolean saveGame(GameState gameState, Configuration config, String playerName) {
        try (ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream(FileConstants.CURRENT_GAME_FILE))) {
            
            // Crear objeto con todos los datos necesarios para restaurar el juego
            SavedGame savedGame = new SavedGame(
                gameState,
                config,
                playerName,
                System.currentTimeMillis() // timestamp para el tiempo transcurrido
            );
            
            out.writeObject(savedGame);
            return true;
            
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Carga el último juego guardado
    // @return SavedGame con el estado del juego o null si no hay juego guardado
    public static SavedGame loadGame(String playerName) {
        try (ObjectInputStream in = new ObjectInputStream(
                new FileInputStream(FileConstants.CURRENT_GAME_FILE))) {
            
            SavedGame savedGame = (SavedGame) in.readObject();
            
            // Verificar que el juego pertenezca al jugador actual
            if (!savedGame.getPlayerName().equals(playerName)) {
                return null;
            }
            
            return savedGame;
            
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }
    
    // Clase para almacenar todos los datos necesarios del juego
    public static class SavedGame implements Serializable {
        private static final long serialVersionUID = 1L;
        
        private GameState gameState;
        private Configuration config;
        private String playerName;
        private long timestamp;
        
        public SavedGame(GameState gameState, Configuration config, 
                        String playerName, long timestamp) {
            this.gameState = gameState;
            this.config = config;
            this.playerName = playerName;
            this.timestamp = timestamp;
        }
        
        // Getters
        public GameState getGameState() { return gameState; }
        public Configuration getConfig() { return config; }
        public String getPlayerName() { return playerName; }
        public long getTimestamp() { return timestamp; }
    }
}