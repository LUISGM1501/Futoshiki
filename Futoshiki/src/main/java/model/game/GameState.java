package model.game;

import java.util.Stack;

public class GameState {
    private FutoshikiBoard board;
    private String difficulty;
    private Stack<Move> moves;         // Para deshacer/rehacer
    private Stack<Move> undoneMoves;   // Jugadas deshechas
    private boolean isGameStarted;
    private String config;
    // Constructor
    public GameState(FutoshikiBoard board, String difficulty, String config) {
        this.board = board;
        this.difficulty = difficulty;
        this.moves = new Stack<>();
        this.undoneMoves = new Stack<>();
        this.config = config;
    }

    // Getters y setters
    public FutoshikiBoard getBoard() {
        return board;
    }

    public void setBoard(FutoshikiBoard board) {
        this.board = board;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getConfig() {
        return config;
    }
}