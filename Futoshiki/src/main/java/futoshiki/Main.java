package futoshiki;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.*;

import controller.config.ConfigurationController;
import controller.game.GameController;
import controller.top10.ScoreController;
import model.config.Configuration;
import model.game.FutoshikiBoard;
import model.game.GameState;
import model.player.PlayerManager;
import view.dialogs.PlayerLoginDialog;
import view.game.MainWindow;

public class Main {
    public static void main(String[] args) {
        try {
            // Usar look and feel más moderno
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            // Configurar fuentes y colores globales
            configureGlobalUI();
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            // Crear splash screen
            JWindow splash = createSplashScreen();
            splash.setVisible(true);
            try {
                Thread.sleep(2000); // Mostrar splash por 2 segundos

                // Inicializar componentes del juego
                PlayerManager playerManager = new PlayerManager();
                Configuration config = new Configuration();
                GameState gameState = new GameState(new FutoshikiBoard(), "Facil");
                MainWindow mainWindow = new MainWindow();

                // Crear y configurar controladores
                ConfigurationController configController =
                    new ConfigurationController(config, mainWindow);
                GameController gameController =
                    new GameController(gameState, mainWindow);
                ScoreController scoreController = new ScoreController();

                // Mostrar diálogo de login
                PlayerLoginDialog loginDialog = new PlayerLoginDialog(null, playerManager);
                loginDialog.setLocationRelativeTo(null);

                // Cerrar splash y mostrar login
                splash.dispose();
                loginDialog.setVisible(true);

                // Cuando se cierra el login, configurar y mostrar ventana principal
                if (loginDialog.isLoggedIn()) {
                    config.setPlayerName(loginDialog.getPlayerName());
                } else {
                    config.setPlayerName("Invitado");
                }

                // Inicializar la ventana principal
                mainWindow.initializeControllers(configController, gameController, scoreController);
                mainWindow.setPlayerName(config.getPlayerName());
                mainWindow.setVisible(true);

            } catch (InterruptedException e) {
                e.printStackTrace();
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