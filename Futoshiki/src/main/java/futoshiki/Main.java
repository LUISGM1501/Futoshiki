package futoshiki;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import controller.config.ConfigurationController;
import controller.game.GameController;
import controller.top10.ScoreController;
import model.config.Configuration;
import model.game.GameState;
import view.game.MainWindow;

public class Main {
    public static void main(String[] args) {
        try {
            // Intentar usar el look and feel del sistema
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            // Crear instancias iniciales
            Configuration config = new Configuration();
            GameState gameState = new GameState();
            
            // Crear la ventana principal
            MainWindow mainWindow = new MainWindow();
            
            // Crear controladores
            ConfigurationController configController = new ConfigurationController(config, mainWindow);
            GameController gameController = new GameController(gameState, mainWindow);
            ScoreController scoreController = new ScoreController();
            
            // Inicializar la ventana principal
            mainWindow.initializeControllers(configController, gameController, scoreController);
            mainWindow.setVisible(true);
        });
    }
}