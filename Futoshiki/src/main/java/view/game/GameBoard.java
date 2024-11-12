package view.game;

import javax.swing.JPanel;
import java.awt.GridLayout;
import view.components.NumberCell;
import view.components.InequalityLabel;

public class GameBoard extends JPanel {
    private int size;
    private NumberCell[][] cells;
    private InequalityLabel[][] horizontalInequalities;
    private InequalityLabel[][] verticalInequalities;

    public GameBoard() {
        this(5); // Tamaño predeterminado 5x5
    }

    public GameBoard(int size) {
        this.size = size;
        setLayout(new GridLayout(size, size));
        initializeComponents();
    }

    private void initializeComponents() {
        cells = new NumberCell[size][size];
        horizontalInequalities = new InequalityLabel[size][size-1];
        verticalInequalities = new InequalityLabel[size-1][size];

        // Inicializar celdas
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                cells[i][j] = new NumberCell();
                add(cells[i][j]);
            }
        }
    }

    // Métodos para manejar el tablero
}