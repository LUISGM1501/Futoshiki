package view.game;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTable;

import model.game.GameScore;

public class ScoreboardView extends JPanel {
    private JTable scoreTable;
    private JComboBox<String> difficultyFilter;
    private JComboBox<Integer> gridSizeFilter;

    public ScoreboardView() {
        setLayout(new BorderLayout());
        initializeComponents();
    }

    private void initializeComponents() {
        // Implementación de la vista del Top 10
    }

    public void updateScores(List<GameScore> scores) {
        // Actualizar la tabla con nuevas puntuaciones
    }
}