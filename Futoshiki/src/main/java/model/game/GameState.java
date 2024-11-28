package model.game;

import java.util.Stack;

public class GameState {
    private FutoshikiBoard board;
    private String difficulty;
    private Stack<Move> moves;         // Para deshacer/rehacer
    private Stack<Move> undoneMoves;   // Jugadas deshechas
    private boolean isGameStarted;
    private String config;

    /**
     * Constructor de la clase GameState.
     * 
     * @param board El tablero de Futoshiki.
     * @param difficulty La dificultad del juego.
     * @param config La configuración del juego.
     */
    public GameState(FutoshikiBoard board, String difficulty, String config) {
        this.board = board;
        this.difficulty = difficulty;
        this.moves = new Stack<>();
        this.undoneMoves = new Stack<>();
        this.config = config;
    }

    // Getters y setters

    /**
     * Obtiene el tablero de Futoshiki.
     * 
     * @return El tablero de Futoshiki.
     */
    public FutoshikiBoard getBoard() {
        return board;
    }

    /**
     * Establece el tablero de Futoshiki.
     * 
     * @param board El tablero de Futoshiki a establecer.
     */
    public void setBoard(FutoshikiBoard board) {
        this.board = board;
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
     * Establece la dificultad del juego.
     * 
     * @param difficulty La dificultad del juego a establecer.
     */
    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * Obtiene la configuración del juego.
     * 
     * @return La configuración del juego.
     */
    public String getConfig() {
        return config;
    }
}