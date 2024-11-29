package controller.top10;

import java.util.List;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import model.game.GameScore;
import model.player.PlayerManager;
import persistence.Top10Manager;
import view.game.ScoreboardView;

/**
 * Controlador para gestionar las puntuaciones del Top 10.
 */
public class ScoreController {
    private List<GameScore> scores;
    private ScoreboardView view;
    private JFrame mainWindow;
    private Top10Manager top10Manager;

    /**
     * Constructor del ScoreController.
     * 
     * @param mainWindow La ventana principal de la aplicación.
     */
    public ScoreController(JFrame mainWindow) {
        this.mainWindow = mainWindow;
        this.view = ScoreboardView.getInstance();
        this.top10Manager = Top10Manager.getInstance();
    }

    /**
     * Intenta añadir una puntuación al Top 10.
     * 
     * @param playerName El nombre del jugador.
     * @param hours Las horas transcurridas.
     * @param minutes Los minutos transcurridos.
     * @param seconds Los segundos transcurridos.
     * @param difficulty La dificultad del juego.
     * @param gridSize El tamaño de la cuadrícula.
     * @return true si la puntuación se añadió al Top 10, false en caso contrario.
     */
    public boolean tryAddScore(String playerName, int hours, int minutes, int seconds, 
                             String difficulty, int gridSize) {
        // No permitir scores de jugadores invitados
        PlayerManager playerManager = PlayerManager.getInstance();
        if (!playerManager.isRegisteredPlayer(playerName)) {
            JOptionPane.showMessageDialog(mainWindow,
                "Los jugadores invitados no pueden guardar puntuaciones en el Top 10",
                "Información",
                JOptionPane.INFORMATION_MESSAGE);
            return false;
        }

        GameScore newScore = new GameScore(playerName, hours, minutes, seconds, difficulty, gridSize);
        if (top10Manager.wouldQualifyForTop10(difficulty, 
            hours * 3600 + minutes * 60 + seconds, gridSize)) {
            
            top10Manager.addScore(newScore);
            JOptionPane.showMessageDialog(mainWindow,
                "¡Felicitaciones! Has entrado al Top 10",
                "Top 10",
                JOptionPane.INFORMATION_MESSAGE);
            return true;
        }
        
        return false;
    }

    /**
     * Muestra el Top 10 de puntuaciones.
     */
    public void showTop10() {
        view.updateScores();
        JDialog dialog = new JDialog(mainWindow, "Top 10", true);
        dialog.setContentPane(view);
        dialog.pack();
        dialog.setLocationRelativeTo(mainWindow);
        dialog.setVisible(true);
    }
}
