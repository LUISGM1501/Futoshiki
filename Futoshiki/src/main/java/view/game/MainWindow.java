package view.game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import controller.config.ConfigurationController;
import controller.game.GameController;
import controller.timer.TimerController;
import controller.top10.ScoreController;
import view.components.TimerDisplay;
public class MainWindow extends JFrame {
    private static final long serialVersionUID = 1L;
    
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
        "CARGAR JUEGO"
    };
    
    // Controladores
    private ConfigurationController configController;
    private GameController gameController;
    private ScoreController scoreController;
    private TimerController timerController;

    public MainWindow() {
        setTitle("Futoshiki");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initializeComponents();
        layoutComponents();
        pack();
        setLocationRelativeTo(null);
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

        
        // Panel de botones
        buttonPanel = new JPanel(new GridLayout(7, 1, 5, 5));
        gameButtons = new JButton[buttonLabels.length];
        
        for (int i = 0; i < buttonLabels.length; i++) {
            gameButtons[i] = createStyledButton(buttonLabels[i]);
            buttonPanel.add(gameButtons[i]);
            
            // Deshabilitar todos los botones excepto INICIAR JUEGO y CARGAR JUEGO
            if (i != 0 && i != 6) {
                gameButtons[i].setEnabled(false);
            }
        }

    }

    private void startTimer() {
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                timerController.actualizarTiempo();
                setTimer(timerController.toString());
            }
        });
        timer.start();
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(150, 30));
        button.setFont(new Font("Arial", Font.PLAIN, 12));
        button.setFocusPainted(false);
        return button;
    }

    private void layoutComponents() {
        // Agregar componentes al panel principal
        mainPanel.add(topPanel, BorderLayout.NORTH);
        
        // Panel central con el juego y panel de dígitos
        JPanel centerPanel = new JPanel(new BorderLayout(10, 0));
        centerPanel.add(gameBoard, BorderLayout.CENTER);
        centerPanel.add(digitPanel, BorderLayout.EAST);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        // Panel de botones a la derecha
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(buttonPanel, BorderLayout.NORTH);
        mainPanel.add(rightPanel, BorderLayout.EAST);
        
        // Agregar el panel principal al frame
        setContentPane(mainPanel);
    }

    public void initializeControllers(ConfigurationController configController,
                                      GameController gameController,
                                      ScoreController scoreController, TimerController timerController) {
        this.configController = configController;
        this.gameController = gameController;
        this.scoreController = scoreController;
        this.timerController = timerController;
        
        setupButtonListeners();
        startTimer();
    }



    private void setupButtonListeners() {
        gameButtons[0].addActionListener(e -> gameController.startGame());
        gameButtons[1].addActionListener(e -> gameController.undoMove());
        gameButtons[2].addActionListener(e -> gameController.redoMove());
        gameButtons[3].addActionListener(e -> gameController.clearGame());
        gameButtons[4].addActionListener(e -> gameController.endGame());
        gameButtons[5].addActionListener(e -> gameController.saveGame());
        gameButtons[6].addActionListener(e -> gameController.loadGame());
    }

    public void enableGameButtons(boolean enabled) {
        for (int i = 1; i < gameButtons.length - 1; i++) {
            gameButtons[i].setEnabled(enabled);
        }
        // INICIAR JUEGO y CARGAR JUEGO se comportan al revés
        gameButtons[0].setEnabled(!enabled);
        gameButtons[6].setEnabled(!enabled);
    }

    public void setTimer(String time) {
        timerLabel.setText(time);
    }

    public void setPlayerName(String name) {
        playerNameLabel.setText("Nombre del jugador: " + name);
    }

    public void setLevel(String level) {
        levelLabel.setText("NIVEL " + level.toUpperCase());
    }


}