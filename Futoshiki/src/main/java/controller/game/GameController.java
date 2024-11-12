package controller.game;

import model.game.GameState;
import model.timer.GameTimer;
import view.game.MainWindow;

public class GameController {
    private GameState gameState;
    private MainWindow view;
    private GameTimer timer;

    // Constructor
    public GameController(GameState gameState, MainWindow view) {
        this.gameState = gameState;
        this.view = view;
    }

    // Métodos para controlar el flujo del juego

    public void startGame() {
        // Iniciar el juego
    }

    public void undoMove() {
        // Deshacer el último movimiento
    }

    public void redoMove() {
        // Rehacer el último movimiento
    }

    public void clearGame() {
        // Limpiar el juego
    }   

    public void endGame() {
        // Finalizar el juego
    }

    public void saveGame() {
        // Guardar el juego
    }

    public void loadGame() {
        // Cargar el juego
    }
}
