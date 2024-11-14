package controller.top10;

import java.util.List;

import javax.swing.JDialog;
import javax.swing.JFrame;

import model.game.GameScore;
import view.game.ScoreboardView;

public class ScoreController {
    private List<GameScore> scores;
    private ScoreboardView view;
    private JFrame mainWindow;

    public ScoreController(JFrame mainWindow) {
        this.mainWindow = mainWindow;
        this.view = new ScoreboardView();
    }

    public void showTop10() {
        JDialog dialog = new JDialog(mainWindow, "Top 10", true);
        dialog.setContentPane(view);
        dialog.pack();
        dialog.setLocationRelativeTo(mainWindow);
        dialog.setVisible(true);
    }
}
