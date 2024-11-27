package controller.game;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Stack;

import javax.swing.JOptionPane;

import controller.timer.TimerController;
import model.config.Configuration;
import model.game.FutoshikiBoard;
import model.game.GameState;
import model.game.Move;
import model.timer.GameTimer;
import persistence.GameSaver;
import persistence.Top10Manager;
import persistence.XMLHandler;
import persistence.XMLHandler.GameData;
import util.constants.MessageConstants;
import view.dialogs.GameSetupDialog;
import view.game.MainWindow;

public class GameController {
    private GameState gameState;
    private MainWindow view;
    private GameTimer timer;
    private TimerController tCGuardado;
    private Map<String, List<GameData>> availableGames;
    private Random random;
    private Configuration config;
    private Stack<Move> moves;
    private Stack<Move> redoMoves;
    private int horas;
    private int minutos;
    private int segundos;
    private int selectedDigit;
    private int selectedSize;
    private String selectedDifficulty;
    private boolean isMultiNivel;
    private boolean isGameStarted;
    private List<GameData> gamesForSize;
    private Top10Manager top10Manager;
    private long startTime;
    private String timerType;

    public GameController(GameState gameState, MainWindow view) {
        this.gameState = gameState;
        this.view = view;
        this.random = new Random();
        this.moves = new Stack<>();
        this.redoMoves = new Stack<>();
        this.selectedDigit = 0;
        this.isGameStarted = false;
        this.top10Manager = new Top10Manager();
        loadAvailableGames();
        
        // Configurar timer
    }

    private void loadAvailableGames() {
        this.availableGames = XMLHandler.loadGames();
    }

    public void startGame() {
        view.stopTimer();
        view.restartTimer();
        GameSetupDialog dialog = new GameSetupDialog(view);
        dialog.setVisible(true);
        config  = new Configuration();
        if (dialog.isConfirmed()) {
            selectedDifficulty = dialog.getSelectedDifficulty();
            selectedSize = dialog.getSelectedSize();
            config.setTimerType(dialog.getTimerType());
            isMultiNivel = dialog.isMultiNivel();
            if(dialog.getTimerType() == "Temporizador")
            {
                config.setTimerHours(dialog.getHours());
                config.setTimerMinutes(dialog.getMinutes());
                config.setTimerSeconds(dialog.getSeconds());
            }else
            {
                config.setTimerHours(0);
                config.setTimerMinutes(0);
                config.setTimerSeconds(0);
            }

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
            initializeNewGame(gamesForSize, selectedDifficulty, selectedSize);

        }
    }

    private void initializeNewGame(List<GameData> gamesForSize, String difficulty, int size) {
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
        
        // Actualizar la vista
        view.setLevel(difficulty);
        view.setTimerType(config);
        view.enableGameButtons(true);
        view.getGameBoard().setPlayable(true);
        view.getGameBoard().setSize(size);
        view.startTimer();
        
        // Inicializar timer y tablero
        startTimer();
        updateGameBoard();
    }

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

    public void setSelectedDigit(int digit) {
        this.selectedDigit = digit;
        view.getDigitPanel().setSelectedDigit(digit);
    }

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

    public void saveGame() {
        if (!isGameStarted) {
            return;
        }
        tCGuardado = view.getTimer();
        horas = tCGuardado.getHoursPassed();
        minutos = tCGuardado.getMinutesPassed();
        segundos = tCGuardado.getSecondsPassed();
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

            tCGuardado.setHoursPassed(horas);
            tCGuardado.setMinutesPassed(minutos);
            tCGuardado.setSecondsPassed(segundos);
            view.resumeTimer(tCGuardado);
            view.startTimer();
        } else {
            JOptionPane.showMessageDialog(view,
                "No se encontró ningún juego guardado",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    public void endGame() {
        int option = JOptionPane.showConfirmDialog(view,
            MessageConstants.CONFIRM_END_GAME,
            "Confirmar",
            JOptionPane.YES_NO_OPTION);
            
        if (option == JOptionPane.YES_OPTION) {
            stopTimer();
            isGameStarted = false;
            view.enableGameButtons(false);
            view.getGameBoard().setPlayable(false);
            startGame();
        }
    }


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

    private void handleGameCompletion() {
        if(isMultiNivel)
        {



            selectedDifficulty = nextDifficulty(selectedDifficulty);

            List<GameData> availableGamesForConfig = availableGames.get(selectedDifficulty);
            gamesForSize = availableGamesForConfig.stream()
                    .filter(game -> game.getTamano() == selectedSize)
                    .toList();
            initializeNewGame(gamesForSize, selectedDifficulty, selectedSize);
        }else
        {
            stopTimer();
            view.stopTimer();
            JOptionPane.showMessageDialog(view,
                    MessageConstants.SUCCESS_GAME_COMPLETED,
                    "¡Felicitaciones!",
                    JOptionPane.INFORMATION_MESSAGE);

            // Calcular tiempo total
            int totalSeconds = (int)((System.currentTimeMillis() - startTime) / 1000);

            // Verificar si califica para el Top 10
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

            isGameStarted = false;
            view.enableGameButtons(false);
            view.getGameBoard().setPlayable(false);
        }

    }

    private void startTimer() {
        //timer.start();
    }

    private void stopTimer() {
        //timer.stop();
    }

    private void restartTimer() {
        //timer.restart();
    }

    private void updateGameBoard() {
        FutoshikiBoard board = gameState.getBoard();
        view.getGameBoard().updateBoard(board);
    }




    public boolean isGameStarted() {
        return isGameStarted;
    }

    public void setGameStarted(boolean gameStarted) {
        isGameStarted = gameStarted;
    }
}
