package controller.top10;

import java.util.List;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import model.game.GameScore;
import persistence.Top10Manager;
import view.game.ScoreboardView;

public class ScoreController {
    private List<GameScore> scores;
    private ScoreboardView view;
    private JFrame mainWindow;
    private Top10Manager top10Manager;

    public ScoreController(JFrame mainWindow) {
        this.mainWindow = mainWindow;
        this.view = ScoreboardView.getInstance();
        this.top10Manager = Top10Manager.getInstance();
    }

    public boolean tryAddScore(String playerName, int hours, int minutes, int seconds, 
                             String difficulty, int gridSize) {
        // No permitir scores de jugadores invitados
        if (playerName.equals("Invitado")) {
            JOptionPane.showMessageDialog(mainWindow,
                "Los jugadores invitados no pueden guardar puntuaciones en el Top 10",
                "Información",
                JOptionPane.INFORMATION_MESSAGE);
            return false;
        }

        GameScore newScore = new GameScore(playerName, hours, minutes, seconds, difficulty, gridSize);
        if (top10Manager.wouldQualifyForTop10(difficulty, 
            hours * 3600 + minutes * 60 + seconds)) {
            
            top10Manager.addScore(newScore);
            JOptionPane.showMessageDialog(mainWindow,
                "¡Felicitaciones! Has entrado al Top 10",
                "Top 10",
                JOptionPane.INFORMATION_MESSAGE);
            return true;
        }
        
        return false;
    }

    public void showTop10() {
        view.updateScores();
        JDialog dialog = new JDialog(mainWindow, "Top 10", true);
        dialog.setContentPane(view);
        dialog.pack();
        dialog.setLocationRelativeTo(mainWindow);
        dialog.setVisible(true);
    }
}
