package view.game;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.game.Celda;

public class GameBoard extends JPanel {
    private int size;
    private JPanel[][] cellPanels;  // Para contener celda y sus desigualdades
    private Celda[][] cells;
    private boolean isPlayable;

    public GameBoard(int size) {
        this.size = size;
        this.isPlayable = false;
        initializeComponents();
        layoutComponents();
    }

    private void initializeComponents() {
        cellPanels = new JPanel[size][size];
        cells = new Celda[size][size];
        
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                cells[i][j] = new Celda(0);
                cellPanels[i][j] = createCellPanel(cells[i][j]);
            }
        }
    }

    private JPanel createCellPanel(Celda celda) {
        JPanel panel = new JPanel(new BorderLayout());
        JButton cellButton = new JButton();
        cellButton.setPreferredSize(new Dimension(50, 50));
        
        // Labels para desigualdades
        JLabel rightLabel = new JLabel(celda.getDesDer());
        JLabel bottomLabel = new JLabel(celda.getDesAbajo());
        
        panel.add(cellButton, BorderLayout.CENTER);
        panel.add(rightLabel, BorderLayout.EAST);
        panel.add(bottomLabel, BorderLayout.SOUTH);
        
        return panel;
    }

    private void layoutComponents() {
        setLayout(new GridLayout(size, size));
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                add(cellPanels[i][j]);
            }
        }
    }

    public void setPlayable(boolean playable) {
        this.isPlayable = playable;
        // Habilitar/deshabilitar interacción
    }

    public void setCellValue(int row, int col, int value) {
        cells[row][col].setValor(value);
        updateCellDisplay(row, col);
    }

    private void updateCellDisplay(int row, int col) {
        // Actualizar la visualización de la celda
    }
}