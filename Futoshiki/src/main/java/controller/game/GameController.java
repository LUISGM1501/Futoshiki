package controller.game;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Stack;

import javax.swing.JOptionPane;

import org.w3c.dom.Element;

import controller.config.ConfigurationController;
import controller.timer.TimerController;
import model.config.Configuration;
import model.game.FutoshikiBoard;
import model.game.FutoshikiGenerator;
import model.game.GameScore;
import model.game.GameState;
import model.game.Move;
import persistence.GameSaver;
import persistence.Top10Manager;
import persistence.XMLHandler;
import persistence.XMLHandler.GameData;
import util.constants.MessageConstants;
import view.dialogs.GameSetupDialog;
import view.game.MainWindow;

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
    private TimerController timerController;

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
        this.timerController = new TimerController(view, this);
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
        System.out.println("=== INICIO DE STARTGAME ===");
        view.stopTimer();
        view.restartTimer();
        GameSetupDialog dialog = new GameSetupDialog(view);
        dialog.setVisible(true);
        
        if (dialog.isConfirmed()) {
            selectedDifficulty = dialog.getSelectedDifficulty();
            selectedSize = dialog.getSelectedSize();
            isMultiNivel = dialog.isMultiNivel();
            
            System.out.println("Configuración seleccionada:");
            System.out.println("- Tamaño: " + selectedSize);
            System.out.println("- Dificultad: " + selectedDifficulty);
            System.out.println("- Multinivel: " + isMultiNivel);

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
            view.setLevel(selectedDifficulty);
            
            // PRINT 1: Antes de inicializar
            System.out.println("\nEstado antes de inicializar juego:");
            System.out.println("- isGameStarted: " + isGameStarted);
            System.out.println("- isPlayable: " + view.getGameBoard().isPlayable());

            if (selectedSize >= 6) {
                System.out.println("\n=== INICIANDO JUEGO GENERADO ===");
                FutoshikiBoard board = FutoshikiGenerator.generateGame(selectedSize, selectedDifficulty);
                gameState.setBoard(board);
                gameState.setDifficulty(selectedDifficulty);
                isGameStarted = true;
                
                // PRINT 2: Después de generar tablero
                System.out.println("\nTablero generado:");
                System.out.println("- Tamaño: " + board.getSize());
                System.out.println("- Estado isGameStarted: " + isGameStarted);
                
                // Habilitar jugabilidad
                view.getGameBoard().setPlayable(true);
                view.enableGameButtons(true);
                
                // PRINT 3: Después de habilitar jugabilidad
                System.out.println("\nEstado después de habilitar jugabilidad:");
                System.out.println("- isPlayable: " + view.getGameBoard().isPlayable());
                
                // Actualizar visualización
                view.getGameBoard().updateBoard(board);
                
                // PRINT 4: Estado final
                System.out.println("\nEstado final del juego:");
                System.out.println("- isGameStarted: " + isGameStarted);
                System.out.println("- isPlayable: " + view.getGameBoard().isPlayable());
            } else {
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
        FutoshikiBoard board;
        
        // IMPORTANTE: Detener y reiniciar timer antes de empezar
        view.stopTimer();
        view.restartTimer();
        
        startTime = System.currentTimeMillis();
        System.out.println("Inicializando nuevo juego:");
        System.out.println("- Tamaño: " + size + "x" + size);
        System.out.println("- Dificultad: " + difficulty);
        System.out.println("- Tiempo inicial: " + startTime);

        if (size >= 6) {
            System.out.println("Generando tablero " + size + "x" + size);
            board = FutoshikiGenerator.generateGame(size, difficulty);
            System.out.println("Tablero generado, actualizando vista...");
            
            // Verificar que el tablero se generó correctamente
            if (board != null) {
                // Imprimir estado del tablero para debug
                System.out.println("Estado del tablero generado:");
                for (int i = 0; i < size; i++) {
                    for (int j = 0; j < size; j++) {
                        if (board.isConstant(i, j)) {
                            System.out.print("[" + board.getValue(i, j) + "] ");
                        } else {
                            System.out.print(board.getValue(i, j) + " ");
                        }
                    }
                    System.out.println();
                }
            }
        } else {
            // Seleccionar partida aleatoria del XML para tamaños 3x3 a 5x5
            GameData selectedGame = gamesForSize.get(random.nextInt(gamesForSize.size()));
            board = createBoard(selectedGame);
        }

        
        // Configurar estado del juego
        gameState.setBoard(board);
        gameState.setDifficulty(difficulty);
        
        // Reiniciar estructuras de control
        moves.clear();
        redoMoves.clear();
        
        // IMPORTANTE: Establecer el estado de juego antes de actualizar la vista
        isGameStarted = true;
        
        // Actualizar la vista y el timer
        view.setLevel(difficulty);
        view.setTimerType(config);
        view.setConfiguration(config);
        view.enableGameButtons(true);
        view.getGameBoard().setPlayable(true);
        view.getGameBoard().updateBoard(board);
        
        // IMPORTANTE: Iniciar el timer después de toda la configuración
        timerController.setValores(config); // Asegurarse que el timer tenga la configuración correcta
        view.startTimer();
        
        // Verificación
        System.out.println("Juego inicializado. Estado del timer:");
        System.out.println("- Tiempo inicial: " + startTime);
        System.out.println("- Tiempo actual: " + System.currentTimeMillis());
        System.out.println("- Diferencia: " + (System.currentTimeMillis() - startTime) + "ms");
        System.out.println("Estado final de inicialización:");
        System.out.println("- Juego iniciado: " + isGameStarted);
        System.out.println("- Tablero jugable: " + view.getGameBoard().isPlayable());
        System.out.println("- Tamaño del tablero: " + board.getSize());
        System.out.println("Timer iniciado. Configuración:");
        System.out.println("- Tipo: " + config.getTimerType());
        System.out.println("- Horas: " + config.getTimerHours());
        System.out.println("- Minutos: " + config.getTimerMinutes());
        System.out.println("- Segundos: " + config.getTimerSeconds());
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
    public void handleCellClick(int row, int col) 
    {
        System.out.println("\nGameController - handleCellClick iniciado:");
        System.out.println("  Posición: " + row + "," + col);
        System.out.println("  isGameStarted: " + isGameStarted);
        System.out.println("  selectedDigit: " + selectedDigit);
        System.out.println("  eraserSelected: " + view.getDigitPanel().isEraserSelected());

        if (!isGameStarted) {
            System.out.println("  El juego no ha comenzado, saliendo de handleCellClick.");
            return;
        }

        FutoshikiBoard board = gameState.getBoard();
        System.out.println("  Valor actual en celda: " + board.getValue(row, col));
        System.out.println("  Es celda constante: " + board.isConstant(row, col));
        System.out.println("  Borrador seleccionado: " + view.getDigitPanel().isEraserSelected());
        
        // Verificar si es celda constante

        if (board.isConstant(row, col)) {
            System.out.println("  Error: Intento de modificar una celda constante.");
            JOptionPane.showMessageDialog(view,
                MessageConstants.ERROR_CONSTANT_CELL,
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Manejar borrado
        boolean isErasing = view.getDigitPanel().isEraserSelected();
        System.out.println("Modo borrador: " + isErasing);
        
        if (isErasing) 
        {
            int currentValue = board.getValue(row, col);
            if (currentValue > 0) {
                System.out.println("  Borrando valor en celda: " + currentValue);
                moves.push(new Move(row, col, 0, currentValue));
                redoMoves.clear();
                board.clearCell(row, col);
                updateGameBoard();
                System.out.println("Celda borrada en posición: " + row + "," + col);
            } else {
                System.out.println("  No hay valor para borrar en la celda.");
            }
            return;
        }

        System.out.println("GameController - Procesando jugada normal");
        // Si no hay dígito seleccionado y no es borrado, mostrar error

        if (selectedDigit == 0) {
            System.out.println("  Error: No hay dígito seleccionado.");
            JOptionPane.showMessageDialog(view,
                MessageConstants.ERROR_NO_DIGIT_SELECTED,
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Manejar colocación de dígito
        int previousValue = board.getValue(row, col);

        System.out.println("  Intentando colocar dígito: " + selectedDigit + " en celda con valor previo: " + previousValue);
        String error = validateMove(row, col, selectedDigit);
        if (board.setCellValue(row, col, selectedDigit)) 
        {
            if (error == null) 
            {
            board.setCellValue(row, col, selectedDigit);
            moves.push(new Move(row, col, selectedDigit, previousValue));
            redoMoves.clear();
            updateGameBoard();
            }

            System.out.println("  Dígito colocado exitosamente.");

            if (board.isBoardComplete()) {
                System.out.println("  El tablero está completo. Juego terminado.");
                handleGameCompletion();
            }
        } else {
            System.out.println("  Error al validar la jugada: " + error);
            JOptionPane.showMessageDialog(view, error, "Error", JOptionPane.ERROR_MESSAGE);
            view.getGameBoard().showError(row, col);
        }
        
    }

    /**
     * Valida una jugada en el tablero.
     * 
     * @param row Fila de la celda.
     * @param col Columna de la celda.
     * @param value Valor a colocar en la celda.
     * @return Mensaje de error si la jugada no es válida, de lo contrario null.
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
        
        // Validar desigualdades a la derecha
        String rightIneq = board.getRightInequality(row, col);
        if (!rightIneq.equals(" ")) {
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
        
        // Validar desigualdades a la izquierda
        if (col > 0) {
            String leftIneq = board.getRightInequality(row, col - 1);
            if (!leftIneq.equals(" ")) {
                int leftValue = board.getValue(row, col - 1);
                if (leftValue != 0) {
                    if (leftIneq.equals(">") && leftValue <= value) {
                        return MessageConstants.ERROR_GREATER_CONSTRAINT;
                    }
                    if (leftIneq.equals("<") && leftValue >= value) {
                        return MessageConstants.ERROR_LESSER_CONSTRAINT;
                    }
                }
            }
        }
        
        // Validar desigualdades abajo
        String bottomIneq = board.getBottomInequality(row, col);
        if (!bottomIneq.equals(" ")) {
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
        
        return null;
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

        boolean saved = GameSaver.saveGame(gameState, view.getPlayerName(), config, view.getTimer());
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
            // Verificar que el tamaño sea válido (3-10)
            int savedSize = savedGame.getBoard().getSize();
            if (savedSize < 3 || savedSize > 10) {
                JOptionPane.showMessageDialog(view,
                    "El juego guardado tiene un tamaño inválido: " + savedSize,
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Verificar que el tamaño coincide con la configuración actual
            if (savedSize != config.getGridSize()) {
                JOptionPane.showMessageDialog(view,
                    "El juego guardado es de tamaño " + savedSize + "x" + 
                    savedSize + " pero la configuración actual es de " + 
                    config.getGridSize() + "x" + config.getGridSize() + ".\n" +
                    "Por favor, ajuste el tamaño en la configuración antes de cargar el juego.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            this.gameState = savedGame;
            view.setLevel(savedGame.getDifficulty());
            view.getGameBoard().updateBoard(gameState.getBoard());
            
            // Obtener los valores guardados del timer desde el XML
            Element timerElement = savedGame.getTimerElement();
            if (timerElement != null) {
                int hours = Integer.parseInt(timerElement.getAttribute("hours"));
                int minutes = Integer.parseInt(timerElement.getAttribute("minutes"));
                int seconds = Integer.parseInt(timerElement.getAttribute("seconds"));
                String timerType = timerElement.getAttribute("type");
                
                // Actualizar el timer en la vista
                view.getTimer().setHoursPassed(hours);
                view.getTimer().setMinutesPassed(minutes);
                view.getTimer().setSecondsPassed(seconds);
                view.getTimer().setCronometro(timerType);
                
                // Actualizar el display del timer
                view.updateTimer(hours, minutes, seconds);
                view.startTimer(); // Esto iniciará el timer desde los valores restaurados
            }
            
            isGameStarted = true;
            view.enableGameButtons(true);
            view.getGameBoard().setPlayable(true);
        } else {
            JOptionPane.showMessageDialog(view,
                "No se encontró ningún juego guardado para " + view.getPlayerName(),
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
        long currentTime = System.currentTimeMillis();
        System.out.println("\n=== VERIFICACIÓN TIEMPO FINAL ===");
        System.out.println("Start time: " + startTime);
        System.out.println("Current time: " + currentTime);
        System.out.println("Diferencia en ms: " + (currentTime - startTime));
        
        // Verificar que startTime sea válido
        if (startTime <= 0) {
            System.err.println("ADVERTENCIA: startTime no inicializado correctamente");
            startTime = currentTime - 1000; // Asignar 1 segundo por defecto
        }
        
        int totalSeconds = (int)((currentTime - startTime) / 1000);
        System.out.println("Tiempo total en segundos: " + totalSeconds);

        // Convertir a formato legible
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;
        
        System.out.println("Tiempo formateado: " + 
            String.format("%02d:%02d:%02d", hours, minutes, seconds));

        int size = gameState.getBoard().getSize();
        
        if (top10Manager.wouldQualifyForTop10(gameState.getDifficulty(), totalSeconds, size)) {
            GameScore newScore = new GameScore(
                view.getPlayerName(),
                hours,
                minutes,
                seconds,
                gameState.getDifficulty(),
                size
            );
            
            boolean added = top10Manager.addScore(newScore);
            if (!added) {
                System.err.println("Error al guardar el score en el Top 10");
            }
        }

        if(isMultiNivel) {
            String nextLevel = null;
            if(selectedDifficulty.equals("Facil")) {
                nextLevel = "Intermedio";
            } else if(selectedDifficulty.equals("Intermedio")) {
                nextLevel = "Dificil";
            }

            if(nextLevel != null) {
                String message = nextLevel.equals("Dificil") ? 
                    "¡Excelente! Juego terminado con éxito. Continuando en nivel difícil." :
                    "¡Felicitaciones! Avanzando al siguiente nivel.";
                        
                JOptionPane.showMessageDialog(view,
                    message,
                    "¡Felicitaciones!",
                    JOptionPane.INFORMATION_MESSAGE);

                // Determinar si usar XML o generador basado en el tamaño
                if (size <= 5) {
                    // Usar juegos del XML para tamaños 3x3 a 5x5
                    List<GameData> availableGamesForConfig = availableGames.get(nextLevel);
                    if (availableGamesForConfig != null && !availableGamesForConfig.isEmpty()) {
                        gamesForSize = availableGamesForConfig.stream()
                            .filter(game -> game.getTamano() == size)
                            .toList();
                        
                        if (!gamesForSize.isEmpty()) {
                            selectedDifficulty = nextLevel;
                            initializeNewGame(gamesForSize, nextLevel, size, new GameSetupDialog(view));
                            return;
                        }
                    }
                } else {
                    // Usar generador para tamaños 6x6 en adelante
                    System.out.println("Generando nuevo juego " + size + "x" + size + " de dificultad " + nextLevel);
                    FutoshikiBoard newBoard = FutoshikiGenerator.generateGame(size, nextLevel);
                    
                    // Actualizar estado
                    selectedDifficulty = nextLevel;
                    gameState.setBoard(newBoard);
                    gameState.setDifficulty(nextLevel);
                    
                    // Actualizar vista
                    view.setLevel(nextLevel);
                    view.getGameBoard().updateBoard(newBoard);
                    
                    // Reiniciar timer y estructuras de control
                    moves.clear();
                    redoMoves.clear();
                    startTime = System.currentTimeMillis();
                    if(!config.getTimerType().equals("Temporizador")) {
                        view.restartTimer();
                    }
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
