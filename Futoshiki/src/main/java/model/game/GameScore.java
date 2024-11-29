package model.game;

import java.io.Serializable;

/**
 * Clase que representa la puntuación de un juego.
 */
public class GameScore implements Comparable<GameScore>, Serializable {
    private static final long serialVersionUID = 1L;
    private String playerName;
    private int hours;
    private int minutes;
    private int seconds;
    private String difficulty;
    private int gridSize;

    /**
     * Constructor de la clase GameScore.
     * 
     * @param playerName Nombre del jugador.
     * @param hours Horas transcurridas.
     * @param minutes Minutos transcurridos.
     * @param seconds Segundos transcurridos.
     * @param difficulty Dificultad del juego.
     * @param gridSize Tamaño de la cuadrícula.
     */
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

    /**
     * Obtiene el nombre del jugador.
     * 
     * @return El nombre del jugador.
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Obtiene las horas transcurridas.
     * 
     * @return Las horas transcurridas.
     */
    public int getHours() {
        return hours;
    }

    /**
     * Obtiene los minutos transcurridos.
     * 
     * @return Los minutos transcurridos.
     */
    public int getMinutes() {
        return minutes;
    }

    /**
     * Obtiene los segundos transcurridos.
     * 
     * @return Los segundos transcurridos.
     */
    public int getSeconds() {
        return seconds;
    }

    /**
     * Obtiene la dificultad del juego.
     * 
     * @return La dificultad del juego.
     */
    public String getDifficulty() {
        return difficulty;
    }

    /**
     * Obtiene el tamaño de la cuadrícula.
     * 
     * @return El tamaño de la cuadrícula.
     */
    public int getGridSize() {
        return gridSize;
    }
}