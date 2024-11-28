package view.game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.border.EmptyBorder;

import controller.config.ConfigurationController;
import controller.game.GameController;
import controller.timer.TimerController;
import controller.top10.ScoreController;
import model.config.Configuration;
import persistence.GameSaver;
import util.constants.GameConstants;
import view.components.TimerDisplay;

public class MainWindow extends JFrame {
    // Constantes para estilo
    private static final Color PRIMARY_COLOR = new Color(25, 118, 210);
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 245);
    private static final Color BUTTON_HOVER_COLOR = new Color(66, 165, 245);
    private static final int WINDOW_WIDTH = 1000;
    private static final int WINDOW_HEIGHT = 700;
    
    // Componentes principales
    private MenuBar menuBar;
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
    private String[] buttonLabels = {
        "INICIAR JUEGO",
        "BORRAR JUGADA",
        "REHACER JUGADA",
        "BORRAR JUEGO",
        "TERMINAR JUEGO",
        "GUARDAR JUEGO",
        "CARGAR JUEGO", 
        "TOP 10"
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



    private void createComponents() {
        // Barra de menú
        menuBar = new MenuBar();
        setJMenuBar(menuBar.getMenuBar());

        // Panel superior
        createTopPanel();

        // Panel de juego
        createGamePanel();

        // Panel de botones
        createButtonPanel();
    }




    private void initializeComponents() {

        //Lo creamos arriba para poder utilizar su tecto en timerLabel
        timerDisplay = new TimerDisplay();

        // Inicializar el panel principal
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Panel superior
        topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        levelLabel = new JLabel("NIVEL FÁCIL");
        levelLabel.setFont(new Font("Arial", Font.BOLD, 14));
        playerNameLabel = new JLabel("Nombre del jugador:");
        timerLabel = new JLabel(timerDisplay.toString());
        
        topPanel.add(levelLabel);
        topPanel.add(playerNameLabel);
        topPanel.add(timerLabel);
        
        // Panel del juego
        gamePanel = new JPanel(new BorderLayout());
        gamePanel.setPreferredSize(new Dimension(500, 500));
        gamePanel.setBackground(Color.WHITE);
        
        // Crear la barra de menú
        menuBar = new MenuBar();
        setJMenuBar(menuBar.getMenuBar());

        // Crear paneles principales
        gameBoard = new GameBoard(5);
        digitPanel = new DigitPanel();

    }


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

    private void createGamePanel() {
        gamePanel = new JPanel(new BorderLayout(10, 0));
        gamePanel.setBackground(BACKGROUND_COLOR);

        // Tablero
        gameBoard = new GameBoard(configuration.getGridSize());
        
        // Panel de dígitos
        digitPanel = new DigitPanel();

        // Colocar según configuración
        if (configuration.getDigitPanelPosition().equals(GameConstants.PANEL_RIGHT)) {
            gamePanel.add(gameBoard, BorderLayout.CENTER);
            gamePanel.add(digitPanel, BorderLayout.EAST);
        } else {
            gamePanel.add(gameBoard, BorderLayout.CENTER);
            gamePanel.add(digitPanel, BorderLayout.WEST);
        }
    }

    private void createButtonPanel() {
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.setBorder(new EmptyBorder(0, 10, 0, 10));

        gameButtons = new JButton[buttonLabels.length];

        for (int i = 0; i < buttonLabels.length; i++) {
            gameButtons[i] = createStyledButton(buttonLabels[i]);
            buttonPanel.add(Box.createVerticalStrut(5));
            buttonPanel.add(gameButtons[i]);

            if (i != 0 && i != 6) {
                gameButtons[i].setEnabled(false);
            }
        }
        gameButtons[6].setEnabled(true);
        buttonPanel.add(Box.createVerticalGlue());


    }

    public void setTimerType(Configuration config)
    {
        timerController.setValores(config);
    }


    public void startTimer() {
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                timerController.actualizarTiempo();
                setTimer();
            }
        });
        timer.start();
    }

    public void restartTimer()
    {
        if(timer != null)
        {
            timerController.setSecondsPassed(0);
            timerController.setMinutesPassed(0);
            timerController.setHoursPassed(0);
            setTimer();
        }
    }

    public void stopTimer()
    {
        if(timer != null){timer.stop();}

    }

    public TimerController getTimer()
    {
        return timerController;
    }

    public void resumeTimer(TimerController tcGuardado)
    {
        timerDisplay.updateTime(tcGuardado.getHoursPassed(), tcGuardado.getMinutesPassed(), tcGuardado.getSecondsPassed());
    }

    private void setTimer()
    {
        timerDisplay.updateTime(timerController.getHoursPassed(), timerController.getMinutesPassed(), timerController.getSecondsPassed());
    }

    private void layoutComponents() {
        // Añadir los paneles principales
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(gamePanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.EAST);
    }


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
    private void setupListeners() {
        // Listener para el panel de dígitos
        digitPanel.addDigitSelectListener(digit -> {
            if (gameController != null) {
                gameController.setSelectedDigit(digit);
            }
        });
        
        // Configurar listeners de la barra de menú
        setupMenuListeners();
        
        // Los listeners de los botones se configurarán cuando se inicialicen los controladores
    }

    private void setupMenuListeners() {
        menuBar.addConfigListener(e -> {
            if (configController != null) {
                configController.showConfigDialog();
            }
        });
        
        menuBar.addPlayListener(e -> {
            if (gameController != null) {
                gameController.startGame();
            }
        });
        
        menuBar.addTop10Listener(e -> {
            if (scoreController != null) {
                scoreController.showTop10();
            }
        });



        menuBar.addHelpListener(e -> showHelp());
        menuBar.addAboutListener(e -> showAbout());

    }



    private void setupButtonListeners() {
        if (gameController == null) return;
        
        gameButtons[0].addActionListener(e -> gameController.startGame());
        gameButtons[1].addActionListener(e -> gameController.undoMove());
        gameButtons[2].addActionListener(e -> gameController.redoMove());
        gameButtons[3].addActionListener(e -> gameController.clearGame());
        gameButtons[4].addActionListener(e -> gameController.endGame());
        gameButtons[5].addActionListener(e -> gameController.saveGame());
        gameButtons[6].addActionListener(e -> gameController.loadGame());
        gameButtons[7].addActionListener(e -> scoreController.showTop10());
    }

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

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setForeground(new Color(33, 33, 33));
        return label;
    }

    // Métodos públicos para controladores y actualizaciones

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

    public void enableGameButtons(boolean enabled) {
        for (int i = 1; i < gameButtons.length - 2; i++) {
            gameButtons[i].setEnabled(enabled);
        }
        // INICIAR JUEGO y CARGAR JUEGO se comportan al revés
        gameButtons[0].setEnabled(!enabled);
        gameButtons[6].setEnabled(!enabled);
        // TOP 10 siempre habilitado
        gameButtons[7].setEnabled(true);
    }

    public void updateTimer(int hours, int minutes, int seconds) {
        timerDisplay.updateTime(hours, minutes, seconds);
    }

    public void setPlayerName(String name) {
        this.playerName = name;
        playerNameLabel.setText("Nombre del jugador: " + name);
    }

    public void setLevel(String level) {
        levelLabel.setText("NIVEL " + level.toUpperCase());
    }

    public GameBoard getGameBoard() {
        return gameBoard;
    }

    public DigitPanel getDigitPanel() {
        return digitPanel;
    }

    public String getPlayerName() {
        return playerName;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration config) {
        this.configuration = config;
        updateConfigurationView();
    }

    private void updateConfigurationView() {
        // Actualizar la vista según la nueva configuración
        if (configuration.getDigitPanelPosition().equals(GameConstants.PANEL_RIGHT)) {
            gamePanel.removeAll();
            gamePanel.add(gameBoard, BorderLayout.CENTER);
            gamePanel.add(digitPanel, BorderLayout.EAST);
        } else {
            gamePanel.removeAll();
            gamePanel.add(gameBoard, BorderLayout.CENTER);
            gamePanel.add(digitPanel, BorderLayout.WEST);
        }
        gamePanel.revalidate();
        gamePanel.repaint();
    }

    private void showHelp() {
        // Mostrar el manual de usuario
    }

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

}