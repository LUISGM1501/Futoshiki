package model.game;

import java.util.Stack;

public class GameState {
    private FutoshikiBoard board;
    private String difficulty;
    private Stack<Move> moves;         // Para deshacer/rehacer
    private Stack<Move> undoneMoves;   // Jugadas deshechas
    private boolean isGameStarted;
}