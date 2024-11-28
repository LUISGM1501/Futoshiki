package controller.game;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Stack;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import model.config.Configuration;
import model.game.FutoshikiBoard;
import model.game.GameState;
import model.game.Move;
import persistence.GameSaver;
import persistence.Top10Manager;
import persistence.XMLHandler;
import persistence.XMLHandler.GameData;
import util.constants.MessageConstants;
import view.dialogs.GameSetupDialog;
import view.game.MainWindow;
import persistence.ConfigurationManager;
import controller.config.ConfigurationController;

/**
 * Controlador del juego Futoshiki.
 */
public class GameController {
    private GameState gameState;
    private MainWindow view;
    private Map<String, List<GameData>> availableGames;
    private Random random;
    private Configuration config;
    private Stack<Move> moves;
    private Stack<Move> redoMoves;
    private int selectedDigit;
    private int selectedSize;
    private String selectedDifficulty;
    private boolean isMultiNivel;
    private boolean isGameStarted;
    private List<GameData> gamesForSize;
    private Top10Manager top10Manager;
    private long startTime;
    private ConfigurationController configController;
    private boolean canPlay;

    /**
     * Constructor del GameController.
     * 
     * @param gameState El estado del juego.
     * @param view La vista principal del juego.
     * @param configController El controlador de configuración.
     */
    public GameController(GameState gameState, MainWindow view, ConfigurationController configController) {
        this.gameState = gameState;
        this.view = view;
        this.configController = configController;
        this.random = new Random();
        this.moves = new Stack<>();
        this.redoMoves = new Stack<>();
        this.selectedDigit = 0;
        this.isGameStarted = false;
        this.top10Manager = new Top10Manager();
        this.canPlay = true;
        
        // Inicializar la configuración con la del ConfigurationController
        this.config = configController.getConfiguration();
        
        loadAvailableGames();
    }

    /**
     * Carga los juegos disponibles desde el XML.
     */
    private void loadAvailableGames() {
        this.availableGames = XMLHandler.loadGames();
    }

    /**
     * Inicia un nuevo juego.
     */
    public void startGame() {
        view.stopTimer();
        view.restartTimer();
        GameSetupDialog dialog = new GameSetupDialog(view);
        dialog.setVisible(true);
        
        if (dialog.isConfirmed()) {
            selectedDifficulty = dialog.getSelectedDifficulty();
            selectedSize = dialog.getSelectedSize();
            isMultiNivel = dialog.isMultiNivel();
            
            // Actualizar la configuración existente en lugar de crear una nueva
            config.setGridSize(selectedSize);
            config.setDifficulty(selectedDifficulty);
            config.setMultiLevel(dialog.isMultiNivel());
            config.setTimerType(dialog.getTimerType());
            config.setDigitPanelPosition(dialog.getSelectedPosition());
            
            if(dialog.getTimerType().equals("Temporizador")) {
                config.setTimerHours(dialog.getHours());
                config.setTimerMinutes(dialog.getMinutes());
                config.setTimerSeconds(dialog.getSeconds());
            } else {
                config.setTimerHours(0);
                config.setTimerMinutes(0);
                config.setTimerSeconds(0);
            }

            // Actualizar el ConfigurationController con la nueva configuración
            configController.updateConfiguration(config);
            
            view.getDigitPanel().setMaxDigits(selectedSize);
            
            // Guardar configuración cuando se inicia un nuevo juego
            System.out.println("GameController: Guardando configuración después de iniciar juego");
            
            // Actualizar la configuración
            config.setGridSize(selectedSize);
            config.setDifficulty(selectedDifficulty);
            config.setTimerType(dialog.getTimerType());
            config.setMultiLevel(dialog.isMultiNivel());
            
            // Actualizar el panel de dígitos antes de inicializar el juego
            view.getDigitPanel().setMaxDigits(selectedSize);
            
            if(dialog.getTimerType().equals("Temporizador")) {
                config.setTimerHours(dialog.getHours());
                config.setTimerMinutes(dialog.getMinutes());
                config.setTimerSeconds(dialog.getSeconds());
            } else {
                config.setTimerHours(0);
                config.setTimerMinutes(0);
                config.setTimerSeconds(0);
            }

            ConfigurationManager.saveConfiguration(config);

            List<GameData> availableGamesForConfig = availableGames.get(selectedDifficulty);
            if (availableGamesForConfig == null || availableGamesForConfig.isEmpty()) {
                JOptionPane.showMessageDialog(view, 
                    MessageConstants.INFO_NO_GAMES_FOR_LEVEL,
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            gamesForSize = availableGamesForConfig.stream()
                .filter(game -> game.getTamano() == selectedSize)
                .toList();

            if (gamesForSize.isEmpty()) {
                JOptionPane.showMessageDialog(view,
                    "NO HAY PARTIDAS PARA ESTE TAMAÑO DE TABLERO",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Inicializar nuevo juego
            initializeNewGame(gamesForSize, selectedDifficulty, selectedSize, dialog);
        }
    }

    /**
     * Inicializa un nuevo juego con los parámetros dados.
     * 
     * @param gamesForSize Lista de juegos disponibles para el tamaño seleccionado.
     * @param difficulty Dificultad seleccionada.
     * @param size Tamaño del tablero.
     * @param dialog Diálogo de configuración del juego.
     */
    private void initializeNewGame(List<GameData> gamesForSize, String difficulty, int size, GameSetupDialog dialog) {
        // Seleccionar partida aleatoria
        GameData selectedGame = gamesForSize.get(random.nextInt(gamesForSize.size()));
        FutoshikiBoard board = createBoard(selectedGame);
        
        // Configurar estado del juego
        gameState.setBoard(board);
        gameState.setDifficulty(difficulty);
        
        // Reiniciar estructuras de control
        moves.clear();
        redoMoves.clear();
        isGameStarted = true;
        startTime = System.currentTimeMillis();
        
        // Obtener la posición seleccionada del diálogo
        String selectedPosition = dialog.getSelectedPosition();
        if (selectedPosition != null) {
            config.setDigitPanelPosition(selectedPosition);
        }
        
        // Actualizar la vista
        view.setLevel(difficulty);
        view.setTimerType(config);
        view.setConfiguration(config); // Asegurarse de que la configuración se actualice
        view.enableGameButtons(true);
        view.getGameBoard().setPlayable(true);
        view.getGameBoard().setSize(size);
        view.updateConfigurationView();
        view.startTimer();
        
        // Inicializar timer y tablero
        startTimer();
        updateGameBoard();
    }

    /**
     * Crea un tablero de Futoshiki a partir de los datos del juego.
     * 
     * @param gameData Datos del juego.
     * @return El tablero de Futoshiki creado.
     */
    private FutoshikiBoard createBoard(GameData gameData) {
        FutoshikiBoard board = new FutoshikiBoard(gameData.getTamano());

        // Colocar constantes
        gameData.getConstantes().forEach((pos, valor) -> {
            String[] coords = pos.split(",");
            int row = Integer.parseInt(coords[0]);
            int col = Integer.parseInt(coords[1]);
            board.setConstant(row, col, valor);
        });

        // Colocar desigualdades
        gameData.getDesigualdades().forEach(inequality -> {
            board.setInequality(
                inequality.getType(),
                inequality.getRow1(),
                inequality.getCol1()
            );
        });

        return board;
    }

    /**
     * Maneja el clic en una celda del tablero.
     * 
     * @param row Fila de la celda.
     * @param col Columna de la celda.
     */
    public void handleCellClick(int row, int col) {
        if (!isGameStarted) {
            return;
        }

        if (selectedDigit == 0) {
            JOptionPane.showMessageDialog(view,
                MessageConstants.ERROR_NO_DIGIT_SELECTED,
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        FutoshikiBoard board = gameState.getBoard();
        
        if (board.isConstant(row, col)) {
            JOptionPane.showMessageDialog(view,
                MessageConstants.ERROR_CONSTANT_CELL,
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        int previousValue = board.getValue(row, col);
        if (board.setCellValue(row, col, selectedDigit)) {
            moves.push(new Move(row, col, selectedDigit, previousValue));
            redoMoves.clear();
            updateGameBoard();
            
            if (board.isBoardComplete()) {
                handleGameCompletion();
            }
        } else {
            // La jugada no es válida, mostrar el error específico
            String error = validateMove(row, col, selectedDigit);
            JOptionPane.showMessageDialog(view, error, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Valida una jugada en el tablero.
     * 
     * @param row Fila de la celda.
     * @param col Columna de la celda.
     * @param value Valor a colocar en la celda.
     * @return Mensaje de error si la jugada no es válida, de lo contrario "JUGADA NO VÁLIDA".
     */
    private String validateMove(int row, int col, int value) {
        FutoshikiBoard board = gameState.getBoard();
        
        // Validar fila
        for (int i = 0; i < board.getSize(); i++) {
            if (i != col && board.getValue(row, i) == value) {
                return MessageConstants.ERROR_DUPLICATE_ROW;
            }
        }
        
        // Validar columna
        for (int i = 0; i < board.getSize(); i++) {
            if (i != row && board.getValue(i, col) == value) {
                return MessageConstants.ERROR_DUPLICATE_COLUMN;
            }
        }
        
        // Validar desigualdades
        String rightIneq = board.getRightInequality(row, col);
        String bottomIneq = board.getBottomInequality(row, col);
        
        if (!rightIneq.equals(" ")) {
            if (col < board.getSize() - 1) {
                int rightValue = board.getValue(row, col + 1);
                if (rightValue != 0) {
                    if (rightIneq.equals(">") && value <= rightValue) {
                        return MessageConstants.ERROR_GREATER_CONSTRAINT;
                    }
                    if (rightIneq.equals("<") && value >= rightValue) {
                        return MessageConstants.ERROR_LESSER_CONSTRAINT;
                    }
                }
            }
        }
        
        if (!bottomIneq.equals(" ")) {
            if (row < board.getSize() - 1) {
                int bottomValue = board.getValue(row + 1, col);
                if (bottomValue != 0) {
                    if (bottomIneq.equals("v") && value <= bottomValue) {
                        return MessageConstants.ERROR_GREATER_CONSTRAINT;
                    }
                    if (bottomIneq.equals("^") && value >= bottomValue) {
                        return MessageConstants.ERROR_LESSER_CONSTRAINT;
                    }
                }
            }
        }
        
        return "JUGADA NO VÁLIDA";
    }

    /**
     * Establece el dígito seleccionado.
     * 
     * @param digit Dígito seleccionado.
     */
    public void setSelectedDigit(int digit) {
        this.selectedDigit = digit;
        view.getDigitPanel().setSelectedDigit(digit);
    }

    /**
     * Deshace el último movimiento.
     */
    public void undoMove() {
        if (!moves.isEmpty()) {
            Move move = moves.pop();
            FutoshikiBoard board = gameState.getBoard();
            
            // Guardar el valor actual antes de deshacerlo
            int currentValue = board.getValue(move.getRow(), move.getColumn());
            
            // Restaurar el valor anterior
            board.clearCell(move.getRow(), move.getColumn());
            if (move.getPreviousValue() > 0) {
                board.setCellValue(move.getRow(), move.getColumn(), move.getPreviousValue());
            }
            
            // Guardar el movimiento para poder rehacerlo
            redoMoves.push(new Move(move.getRow(), move.getColumn(), currentValue, move.getPreviousValue()));
            updateGameBoard();
        } else {
            JOptionPane.showMessageDialog(view,
                MessageConstants.INFO_NO_MORE_MOVES,
                "Información",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Rehace el último movimiento deshecho.
     */
    public void redoMove() {
        if (!redoMoves.isEmpty()) {
            Move move = redoMoves.pop();
            FutoshikiBoard board = gameState.getBoard();
            
            // Guardar el valor actual
            int currentValue = board.getValue(move.getRow(), move.getColumn());
            
            // Realizar el movimiento
            board.setCellValue(move.getRow(), move.getColumn(), move.getValue());
            
            // Agregar a la pila de deshacer
            moves.push(new Move(move.getRow(), move.getColumn(), move.getValue(), currentValue));
            updateGameBoard();
        }
    }

    /**
     * Limpia el tablero del juego.
     */
    public void clearGame() {
        int option = JOptionPane.showConfirmDialog(view,
            MessageConstants.CONFIRM_CLEAR_GAME,
            "Confirmar",
            JOptionPane.YES_NO_OPTION);
            
        if (option == JOptionPane.YES_OPTION) {
            gameState.getBoard().clearNonConstantCells();
            moves.clear();
            redoMoves.clear();
            updateGameBoard();
            restartTimer();
        }
    }

    /**
     * Guarda el estado actual del juego.
     */
    public void saveGame() {
        if (!isGameStarted) {
            return;
        }
        boolean saved = GameSaver.saveGame(gameState, view.getPlayerName(), config);
        if (saved) {
            JOptionPane.showMessageDialog(view,
                "Juego guardado exitosamente",
                "Información",
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(view,
                "Error al guardar el juego",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Carga un juego guardado.
     */
    public void loadGame() {
        if (isGameStarted) {
            return;
        }
        
        GameState savedGame = GameSaver.loadGame(view.getPlayerName());
        if (savedGame != null) {
            this.gameState = savedGame;
            view.setLevel(savedGame.getDifficulty());
            isGameStarted = true;
            updateGameBoard();
            view.enableGameButtons(true);
            view.getGameBoard().setPlayable(true);
            view.startTimer();
        } else {
            JOptionPane.showMessageDialog(view,
                "No se encontró ningún juego guardado",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Termina el juego actual.
     */
    public void endGame() {
        int option = JOptionPane.showConfirmDialog(view,
            MessageConstants.CONFIRM_END_GAME,
            "Confirmar",
            JOptionPane.YES_NO_OPTION);
            
        if (option == JOptionPane.YES_OPTION) {
            isGameStarted = false;
            view.enableGameButtons(false);
            view.getGameBoard().setPlayable(false);
            startGame();
        }
    }

    /**
     * Resuelve el juego actual.
     */
    public void solveGame() {
        if (!isGameStarted) {
            return;
        }

        int option = JOptionPane.showConfirmDialog(view,
            "¿Está seguro que desea ver la solución? Esta acción no se puede deshacer.",
            "Confirmar solución",
            JOptionPane.YES_NO_OPTION);
            
        if (option == JOptionPane.YES_OPTION) {
            FutoshikiBoard board = gameState.getBoard();
            boolean solved = board.solve();
            
            if (solved) {
                updateGameBoard();
                JOptionPane.showMessageDialog(view,
                    "¡Juego resuelto!",
                    "Solución",
                    JOptionPane.INFORMATION_MESSAGE);
                isGameStarted = false;
                view.enableGameButtons(false);
                view.getGameBoard().setPlayable(false);
            } else {
                JOptionPane.showMessageDialog(view,
                    "No se encontró solución para este juego",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Obtiene la siguiente dificultad en el modo multinivel.
     * 
     * @param currentDifficulty Dificultad actual.
     * @return La siguiente dificultad.
     */
    private String nextDifficulty(String currentDifficulty)
    {
        switch (currentDifficulty)
        {
            case "Facil":
                return "Intermedio";

            case "Intermedio":
                return "Dificil";
        }

        return "Dificil";
    }

    /**
     * Maneja la finalización del juego.
     */
    private void handleGameCompletion() {
        // Calcular tiempo total actual
        int totalSeconds = (int)((System.currentTimeMillis() - startTime) / 1000);
        
        // Verificar Top 10 y guardar si califica
        if (top10Manager.wouldQualifyForTop10(gameState.getDifficulty(), totalSeconds)) {
            int hours = totalSeconds / 3600;
            int minutes = (totalSeconds % 3600) / 60;
            int seconds = totalSeconds % 60;
            
            top10Manager.addScore(new model.game.GameScore(
                view.getPlayerName(),
                hours,
                minutes,
                seconds,
                gameState.getDifficulty(),
                gameState.getBoard().getSize()
            ));
        }

        if(isMultiNivel) {
            if(selectedDifficulty.equals("Facil")) {
                // Avanzar al siguiente nivel
                selectedDifficulty = "Intermedio";
            } else if(selectedDifficulty.equals("Intermedio")) {
                // Avanzar al siguiente nivel
                selectedDifficulty = "Dificil";
            }
            // Si ya está en nivel difícil o avanzó a un nuevo nivel
            List<GameData> availableGamesForConfig = availableGames.get(selectedDifficulty);
            if (availableGamesForConfig != null && !availableGamesForConfig.isEmpty()) {
                gamesForSize = availableGamesForConfig.stream()
                    .filter(game -> game.getTamano() == selectedSize)
                    .toList();
                
                if (!gamesForSize.isEmpty()) {
                    String message = selectedDifficulty.equals("Dificil") ? 
                        "¡Excelente! Juego terminado con éxito. Continuando en nivel difícil." :
                        "¡Felicitaciones! Avanzando al siguiente nivel.";
                        
                    JOptionPane.showMessageDialog(view,
                        message,
                        "¡Felicitaciones!",
                        JOptionPane.INFORMATION_MESSAGE);
                        
                    initializeNewGame(gamesForSize, selectedDifficulty, selectedSize, new GameSetupDialog(view));
                    return;
                }
            }
        }
        
        // Si no es multinivel o no hay más niveles disponibles
        finishGame("¡Excelente! Juego terminado con éxito");
    }

    /**
     * Finaliza el juego con un mensaje.
     * 
     * @param message Mensaje a mostrar.
     */
    private void finishGame(String message) {
        JOptionPane.showMessageDialog(view,
                message,
                "¡Felicitaciones!",
                JOptionPane.INFORMATION_MESSAGE);
        isGameStarted = false;
        view.enableGameButtons(false);
        view.getGameBoard().setPlayable(false);
    }

    /**
     * Inicia el temporizador del juego.
     */
    private void startTimer() {
        //timer.start();
    }

    /**
     * Detiene el temporizador del juego.
     */
    private void stopTimer() {
        //timer.stop();
    }

    /**
     * Reinicia el temporizador del juego.
     */
    private void restartTimer() {
        //timer.restart();
    }

    /**
     * Actualiza el tablero del juego en la vista.
     */
    private void updateGameBoard() {
        FutoshikiBoard board = gameState.getBoard();
        view.getGameBoard().updateBoard(board);
    }

    /**
     * Verifica si el juego ha comenzado.
     * 
     * @return true si el juego ha comenzado, false en caso contrario.
     */
    public boolean isGameStarted() {
        return isGameStarted;
    }

    /**
     * Establece el estado de inicio del juego.
     * 
     * @param gameStarted true si el juego ha comenzado, false en caso contrario.
     */
    public void setGameStarted(boolean gameStarted) {
        isGameStarted = gameStarted;
    }
}
