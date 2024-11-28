package futoshiki;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.*;

import controller.config.ConfigurationController;
import controller.game.GameController;
import controller.timer.TimerController;
import controller.top10.ScoreController;
import model.config.Configuration;
import model.game.FutoshikiBoard;
import model.game.GameState;
import model.player.PlayerManager;
import view.dialogs.PlayerLoginDialog;
import view.game.MainWindow;
import persistence.ConfigurationManager;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                configureGlobalUI();
                
                // Mostrar pantalla de carga
                JWindow splashScreen = createSplashScreen();
                splashScreen.setVisible(true);
                
                // Inicializar el PlayerManager
                PlayerManager playerManager = new PlayerManager();
                
                // Mostrar diálogo de login
                PlayerLoginDialog loginDialog = new PlayerLoginDialog(null, playerManager);
                splashScreen.dispose();
                loginDialog.setVisible(true);
                
                // Si el usuario cancela el login, la aplicación termina
                if (!loginDialog.isLoggedIn() && loginDialog.getPlayerName().isEmpty()) {
                    System.exit(0);
                }
                
                // Crear componentes principales
                Configuration defaultConfig = new Configuration();
                defaultConfig.setPlayerName(loginDialog.getPlayerName());
                MainWindow mainWindow = new MainWindow();
                mainWindow.setPlayerName(loginDialog.getPlayerName());
                
                // Crear y configurar controladores
                ConfigurationController configController = new ConfigurationController(defaultConfig, mainWindow);
                TimerController timerController = new TimerController();
                
                GameState gameState = new GameState(
                    new FutoshikiBoard(configController.getConfiguration().getGridSize()),
                    configController.getConfiguration().getDifficulty(),
                    configController.getConfiguration().toString()
                );
                
                GameController gameController = new GameController(gameState, mainWindow, configController);
                ScoreController scoreController = new ScoreController(mainWindow);
                
                // Inicializar la ventana principal con todos los controladores
                mainWindow.initializeControllers(configController, gameController, scoreController, timerController);
                mainWindow.setVisible(true);
                
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,
                    "Error al iniciar la aplicación: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        });
    }

    private static void configureGlobalUI() {
        // Configurar UI global
        UIManager.put("Button.arc", 15);
        UIManager.put("Panel.background", new Color(240, 240, 240));
        UIManager.put("OptionPane.background", new Color(240, 240, 240));
        UIManager.put("Panel.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("Button.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("Label.font", new Font("Segoe UI", Font.PLAIN, 14));
    }

    private static JWindow createSplashScreen() {
        JWindow splash = new JWindow();
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(new Color(41, 128, 185));

        // Título
        JLabel title = new JLabel("Futoshiki", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        // Subtítulo
        JLabel subtitle = new JLabel("Cargando...", SwingConstants.CENTER);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitle.setForeground(Color.WHITE);
        subtitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Progress bar
        JProgressBar progress = new JProgressBar();
        progress.setIndeterminate(true);
        progress.setBackground(new Color(52, 152, 219));
        progress.setForeground(Color.WHITE);
        progress.setBorder(BorderFactory.createEmptyBorder(0, 50, 20, 50));

        content.add(title, BorderLayout.NORTH);
        content.add(progress, BorderLayout.CENTER);
        content.add(subtitle, BorderLayout.SOUTH);

        splash.setContentPane(content);
        splash.setSize(400, 200);
        splash.setLocationRelativeTo(null);
        return splash;
    }
}