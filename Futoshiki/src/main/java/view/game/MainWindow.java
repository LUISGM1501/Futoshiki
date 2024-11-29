package view.game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import controller.config.ConfigurationController;
import controller.game.GameController;
import controller.timer.TimerController;
import controller.top10.ScoreController;
import model.config.Configuration;
import model.player.PlayerManager;
import persistence.ConfigurationManager;
import persistence.GameSaver;
import view.components.TimerDisplay;
import view.dialogs.HelpDialog;
import view.dialogs.PlayerLoginDialog;


public class MainWindow extends JFrame {
    // Constantes para estilo
    private static final Color PRIMARY_COLOR = new Color(25, 118, 210);
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 245);
    private static final Color BUTTON_HOVER_COLOR = new Color(66, 165, 245);
    private static final int WINDOW_WIDTH = 1000;
    private static final int WINDOW_HEIGHT = 700;
    
    // Componentes principales
    private JPanel mainPanel;
    private JPanel gamePanel;
    private DigitPanel digitPanel;
    private GameBoard gameBoard;
    private TimerDisplay timerDisplay;

    private Timer timer;

    private int x = 0;
    
    // Panel superior
    private JPanel topPanel;
    private JLabel levelLabel;
    private JLabel playerNameLabel;
    private JLabel timerLabel;
    
    // Panel de botones
    private JPanel buttonPanel;
    private JButton[] gameButtons;
    private static final String[] BUTTON_LABELS = {
        "INICIAR JUEGO",
        "BORRAR JUGADA",
        "REHACER JUGADA",
        "BORRAR JUEGO",
        "TERMINAR JUEGO",
        "SOLUCIONAR JUEGO",  // Nuevo botón
        "GUARDAR JUEGO",
        "CARGAR JUEGO", 
        "TOP 10",
        "AYUDA",
        "ACERCA DE",
        "CERRAR SESIÓN"  // Agregar el botón de logout al final
    };
    
    // Controladores
    private ConfigurationController configController;
    private GameController gameController;
    private ScoreController scoreController;
    private GameSaver gamseSaver;

    private TimerController timerController;

    
    // Estado y configuración
    private Configuration configuration;
    private String playerName;

    /**
     * Constructor de MainWindow.
     */
    public MainWindow() {
        super("Futoshiki");
        // Primero configuramos la ventana base
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        setBackground(BACKGROUND_COLOR);

        // Inicializamos la configuración
        this.configuration = new Configuration();
        
        // Creamos el panel principal primero
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(mainPanel);

        // Luego inicializamos todos los componentes
        createComponents();
        layoutComponents();
        setupListeners();

        // Finalmente empaquetamos y centramos
        pack();
        setLocationRelativeTo(null);
        
        // Configurar diálogo de cierre
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int option = JOptionPane.showConfirmDialog(
                    MainWindow.this,
                    "¿Está seguro que desea salir?",
                    "Confirmar salida",
                    JOptionPane.YES_NO_OPTION
                );
                if (option == JOptionPane.YES_OPTION) {
                    dispose();
                }
            }
        });
    }

    /**
     * Crea los componentes de la ventana principal.
     */
    private void createComponents() {
        // Crear panel de botones antes que el panel de juego
        createButtonPanel();

        // Panel superior
        createTopPanel();

        // Panel de juego (que ahora puede usar buttonPanel)
        createGamePanel();
    }

    /**
     * Crea el panel superior de la ventana.
     */
    private void createTopPanel() {
        // Panel superior
        topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(BACKGROUND_COLOR);

        // Panel de información
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        infoPanel.setBackground(BACKGROUND_COLOR);

        // Labels
        levelLabel = new JLabel("NIVEL FÁCIL");
        levelLabel.setFont(new Font("Arial", Font.BOLD, 16));
        playerNameLabel = new JLabel("Nombre del jugador: Invitado");
        playerNameLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        // Timer
        timerDisplay = new TimerDisplay();

        // Añadir componentes
        infoPanel.add(levelLabel);
        infoPanel.add(playerNameLabel);

        JPanel timerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        timerPanel.setBackground(BACKGROUND_COLOR);
        timerPanel.add(timerDisplay);

        // Añadir al panel superior
        topPanel.add(infoPanel);
        topPanel.add(timerPanel);
    }

    /**
     * Crea el panel de juego.
     */
    private void createGamePanel() {
        gamePanel = new JPanel(new BorderLayout(10, 0));
        gamePanel.setBackground(BACKGROUND_COLOR);

        // Tablero
        gameBoard = new GameBoard(configuration.getGridSize());
        
        // Panel de dígitos
        digitPanel = new DigitPanel();

        // Panel contenedor izquierdo/derecho que tendrá los dígitos y botones
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        sidePanel.setBackground(BACKGROUND_COLOR);
        sidePanel.add(digitPanel);
        sidePanel.add(buttonPanel);

        // Colocar según configuración
        gamePanel.add(gameBoard, BorderLayout.CENTER);
        
        // Remover paneles existentes para evitar duplicados
        if (mainPanel.getComponentCount() > 0) {
            mainPanel.remove(buttonPanel);
            gamePanel.removeAll();
        }

        // Agregar los paneles en la posición correcta
        if (configuration.getDigitPanelPosition().equals("left")) {
            mainPanel.add(sidePanel, BorderLayout.WEST);
            gamePanel.add(gameBoard, BorderLayout.CENTER);
        } else {
            mainPanel.add(sidePanel, BorderLayout.EAST);
            gamePanel.add(gameBoard, BorderLayout.CENTER);
        }
    }

    /**
     * Actualiza el tamaño del tablero.
     * 
     * @param size El nuevo tamaño del tablero.
     */
    public void updateBoardSize(int size) {
        gameBoard.setSize(size);
        digitPanel.setMaxDigits(size);
        revalidate();
        repaint();
    }

    /**
     * Crea el panel de botones.
     */
    private void createButtonPanel() {
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.setBorder(new EmptyBorder(0, 10, 0, 10));
    
        gameButtons = new JButton[BUTTON_LABELS.length];
    
        for (int i = 0; i < BUTTON_LABELS.length; i++) {
            gameButtons[i] = createStyledButton(BUTTON_LABELS[i]);
            buttonPanel.add(Box.createVerticalStrut(5));
            buttonPanel.add(gameButtons[i]);
    
            if (i != 0 && i != 7) {
                gameButtons[i].setEnabled(false);
            }
        }
        
        gameButtons[7].setEnabled(true); // CARGAR JUEGO siempre habilitado
        buttonPanel.add(Box.createVerticalGlue());
    }

    /**
     * Configura el tipo de temporizador.
     * 
     * @param config La configuración del temporizador.
     */
    public void setTimerType(Configuration config) {
        timerController.setValores(config);
    }

    /**
     * Inicia el temporizador.
     */
    public void startTimer() {
        if (timer != null) {
            timer.stop();
        }
        
        timer = new Timer(1000, e -> {
            timerController.actualizarTiempo();
            updateTimer(timerController.getHoursPassed(), 
                        timerController.getMinutesPassed(), 
                        timerController.getSecondsPassed());
        });
        timer.start();
    }

    /**
     * Reinicia el temporizador.
     */
    public void restartTimer() {
        if (timer != null) {
            timer.restart();
        }
    }

    /**
     * Detiene el temporizador.
     */
    public void stopTimer() {
        if (timer != null) {
            timer.stop();
        }
    }

    /**
     * Obtiene el controlador del temporizador.
     * 
     * @return El controlador del temporizador.
     */
    public TimerController getTimer() {
        return timerController;
    }

    /**
     * Reanuda el temporizador con un controlador guardado.
     * 
     * @param tcGuardado El controlador del temporizador guardado.
     */
    public void resumeTimer(TimerController tcGuardado) {
        // Detener el timer actual si existe
        if (timer != null) {
            timer.stop();
        }
        
        // Actualizar valores del timer
        timerController.setHoursPassed(tcGuardado.getHoursPassed());
        timerController.setMinutesPassed(tcGuardado.getMinutesPassed());
        timerController.setSecondsPassed(tcGuardado.getSecondsPassed());
        timerController.setCronometro(tcGuardado.getCronometro());
        
        // Actualizar display
        timerDisplay.updateTime(tcGuardado.getHoursPassed(), 
                                tcGuardado.getMinutesPassed(), 
                                tcGuardado.getSecondsPassed());
        
        // Reiniciar el timer
        startTimer();
    }

    /**
     * Actualiza la visualización del temporizador.
     */
    private void setTimer() {
        timerDisplay.updateTime(timerController.getHoursPassed(), timerController.getMinutesPassed(), timerController.getSecondsPassed());
    }

    /**
     * Organiza los componentes de la ventana principal.
     */
    private void layoutComponents() {
        // Añadir los paneles principales en orden
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(gamePanel, BorderLayout.CENTER);
        // No necesitamos añadir buttonPanel aquí porque ya se añadió en createGamePanel
    }

    /**
     * Inicializa los controladores de la ventana principal.
     * 
     * @param configController El controlador de configuración.
     * @param gameController El controlador del juego.
     * @param scoreController El controlador de puntuaciones.
     * @param timerController El controlador del temporizador.
     */
    public void initializeControllers(ConfigurationController configController,
                                      GameController gameController,
                                      ScoreController scoreController, TimerController timerController) {
        this.configController = configController;
        this.gameController = gameController;
        this.scoreController = scoreController;
        this.timerController = timerController;
        
        setupButtonListeners();
        gameBoard.addCellClickListener((row, col) ->
                gameController.handleCellClick(row, col));
    }

    /**
     * Configura los listeners de la ventana principal.
     */
    private void setupListeners() {
        // Listener para el panel de dígitos
        digitPanel.addDigitSelectListener(digit -> {
            if (gameController != null) {
                gameController.setSelectedDigit(digit);
            }
        });
        
        // Los listeners de los botones se configurarán cuando se inicialicen los controladores
    }

    /**
     * Configura los listeners de los botones.
     */
    private void setupButtonListeners() {
        if (gameController == null) return;
        
        gameButtons[0].addActionListener(e -> gameController.startGame());        // INICIAR JUEGO
        gameButtons[1].addActionListener(e -> gameController.undoMove());         // BORRAR JUGADA
        gameButtons[2].addActionListener(e -> gameController.redoMove());         // REHACER JUGADA
        gameButtons[3].addActionListener(e -> gameController.clearGame());        // BORRAR JUEGO
        gameButtons[4].addActionListener(e -> gameController.endGame());          // TERMINAR JUEGO
        gameButtons[5].addActionListener(e -> gameController.solveGame());        // SOLUCIONAR JUEGO
        gameButtons[6].addActionListener(e -> gameController.saveGame());         // GUARDAR JUEGO
        gameButtons[7].addActionListener(e -> gameController.loadGame());         // CARGAR JUEGO
        gameButtons[8].addActionListener(e -> scoreController.showTop10());       // TOP 10
        gameButtons[9].addActionListener(e -> showHelp());                        // AYUDA
        gameButtons[10].addActionListener(e -> showAbout());                      // ACERCA DE
        gameButtons[11].addActionListener(e -> handleLogout());                   // Listener para logout
    }

    /**
     * Crea un botón con estilo.
     * 
     * @param text El texto del botón.
     * @return El botón creado.
     */
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(PRIMARY_COLOR);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setMaximumSize(new Dimension(200, 40));
        button.setPreferredSize(new Dimension(200, 40));
        
        // Efectos hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (button.isEnabled()) {
                    button.setBackground(BUTTON_HOVER_COLOR);
                }
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (button.isEnabled()) {
                    button.setBackground(PRIMARY_COLOR);
                }
            }
        });
        
        return button;
    }

    /**
     * Crea un label con estilo.
     * 
     * @param text El texto del label.
     * @return El label creado.
     */
    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setForeground(new Color(33, 33, 33));
        return label;
    }

    // Métodos públicos para controladores y actualizaciones

    /**
     * Inicializa los controladores de la ventana principal.
     * 
     * @param configController El controlador de configuración.
     * @param gameController El controlador del juego.
     * @param scoreController El controlador de puntuaciones.
     */
    public void initializeControllers(
            ConfigurationController configController,
            GameController gameController,
            ScoreController scoreController) {
        this.configController = configController;
        this.gameController = gameController;
        this.scoreController = scoreController;
        
        setupButtonListeners();
        gameBoard.addCellClickListener((row, col) -> 
            gameController.handleCellClick(row, col));
    }

    /**
     * Habilita o deshabilita los botones del juego.
     * 
     * @param enabled Indica si los botones deben estar habilitados o no.
     */
    public void enableGameButtons(boolean enabled) {
        for (int i = 1; i < gameButtons.length - 1; i++) {  // Excluir INICIAR JUEGO y TOP 10
            if (i != 7) { // No deshabilitar CARGAR JUEGO
                gameButtons[i].setEnabled(enabled);
            }
        }
        // INICIAR JUEGO y CARGAR JUEGO se comportan al revés
        gameButtons[0].setEnabled(!enabled);
        gameButtons[7].setEnabled(!enabled);
        // TOP 10 siempre habilitado
        gameButtons[gameButtons.length - 1].setEnabled(true);
    }

    /**
     * Actualiza el temporizador.
     * 
     * @param hours Las horas.
     * @param minutes Los minutos.
     * @param seconds Los segundos.
     */
    public void updateTimer(int hours, int minutes, int seconds) {
        timerDisplay.updateTime(hours, minutes, seconds);
    }

    /**
     * Establece el nombre del jugador.
     * 
     * @param name El nombre del jugador.
     */
    public void setPlayerName(String name) {
        this.playerName = name;
        playerNameLabel.setText("Nombre del jugador: " + name);
    }

    /**
     * Establece el nivel del juego.
     * 
     * @param level El nivel del juego.
     */
    public void setLevel(String level) {
        levelLabel.setText("NIVEL " + level.toUpperCase());
    }

    /**
     * Obtiene el tablero del juego.
     * 
     * @return El tablero del juego.
     */
    public GameBoard getGameBoard() {
        return gameBoard;
    }

    /**
     * Obtiene el panel de dígitos.
     * 
     * @return El panel de dígitos.
     */
    public DigitPanel getDigitPanel() {
        return digitPanel;
    }

    /**
     * Obtiene el nombre del jugador.
     * 
     * @return El nombre del jugador.
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Obtiene la configuración del juego.
     * 
     * @return La configuración del juego.
     */
    public Configuration getConfiguration() {
        return configuration;
    }

    /**
     * Establece la configuración del juego.
     * 
     * @param config La configuración del juego.
     */
    public void setConfiguration(Configuration config) {
        System.out.println("MainWindow: Actualizando configuración");
        this.configuration = config;
        updateConfigurationView();
        // Guardar la configuración cuando se actualiza en la ventana principal
        ConfigurationManager.saveConfiguration(config);
    }

    /**
     * Actualiza la vista según la nueva configuración.
     */
    public void updateConfigurationView() {
        // Actualizar la vista según la nueva configuración
        mainPanel.removeAll();
        gamePanel.removeAll();

        // Actualizar el panel de dígitos con el nuevo tamaño
        digitPanel.setMaxDigits(configuration.getGridSize());

        // Recrear el panel lateral con dígitos y botones
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        sidePanel.setBackground(BACKGROUND_COLOR);
        sidePanel.add(digitPanel);
        sidePanel.add(buttonPanel);

        // Añadir componentes en el orden correcto
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(gamePanel, BorderLayout.CENTER);
        
        // Colocar el panel lateral según la configuración
        if (configuration.getDigitPanelPosition().equals("left")) {
            mainPanel.add(sidePanel, BorderLayout.WEST);
            gamePanel.add(gameBoard, BorderLayout.CENTER);
        } else {
            mainPanel.add(sidePanel, BorderLayout.EAST);
            gamePanel.add(gameBoard, BorderLayout.CENTER);
        }

        // Actualizar la interfaz
        mainPanel.revalidate();
        mainPanel.repaint();
        gamePanel.revalidate();
        gamePanel.repaint();
    }

    /**
     * Muestra el diálogo de ayuda.
     */
    private void showHelp() {
        HelpDialog helpDialog = new HelpDialog(this);
        helpDialog.setVisible(true);
    }

    /**
     * Muestra el diálogo "Acerca de".
     */
    private void showAbout() {
        JOptionPane.showMessageDialog(this,
            "Futoshiki v1.0\n" +
            "Fecha: Noviembre 2024\n" +
            "TEC - Programación Orientada a Objetos\n" +
            "Desarrollado por: Luis Urbina Salazar y Andres Hernandez Campos\n" + 
            "Carnet: 2023156802 y 2024096912 respectivamente",
            "Acerca de",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    /**
     * Maneja el evento de logout.
     */
    private void handleLogout() {
        if (gameController != null && gameController.isGameStarted()) {
            int option = JOptionPane.showConfirmDialog(this,
                "¿Desea guardar el juego actual antes de cerrar sesión?",
                "Guardar Juego",
                JOptionPane.YES_NO_CANCEL_OPTION);
                
            if (option == JOptionPane.CANCEL_OPTION) {
                return;
            } else if (option == JOptionPane.YES_OPTION) {
                gameController.saveGame();
            }
        }

        // Detener timers y limpiar estado
        stopTimer();
        gameBoard.reset();
        digitPanel.setSelectedDigit(0);
        
        // Mostrar diálogo de login
        PlayerLoginDialog loginDialog = new PlayerLoginDialog(this, PlayerManager.getInstance());
        loginDialog.setVisible(true);

        if (loginDialog.isLoggedIn() || !loginDialog.getPlayerName().isEmpty()) {
            playerName = loginDialog.getPlayerName();
            setPlayerName(playerName);
            enableGameButtons(false);
            gameBoard.setPlayable(false);
        }
    }

    /**
     * Reinicializa el juego.
     */
    private void restartGame() {
        // Reinicializar controladores y estado del juego
        initializeControllers(configController, gameController, scoreController, timerController);
        enableGameButtons(false);
        getGameBoard().setPlayable(false);
    }
}