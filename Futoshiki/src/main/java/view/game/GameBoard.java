package view.game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.Border;

import util.constants.GameConstants;
import model.game.Celda;
import model.game.FutoshikiBoard;

/**
 * Clase GameBoard que representa el tablero del juego Futoshiki.
 */
public class GameBoard extends JPanel {
    // Constantes para estilo
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 245);
    private static final Color CELL_BACKGROUND = Color.WHITE;
    private static final Color CELL_BORDER = new Color(200, 200, 200);
    private static final Color CONSTANT_COLOR = new Color(0, 102, 204);
    private static final Color ERROR_COLOR = new Color(255, 102, 102);
    private static final int INEQUALITY_SIZE = 20;
    
    private int size;
    private int cellSize;
    private JPanel[][] cellPanels;
    private JButton[][] cellButtons;
    private JLabel[][] rightLabels;
    private JLabel[][] bottomLabels;
    private boolean isPlayable;
    private List<CellClickListener> listeners;
    private Point lastErrorCell;

    /**
     * Constructor de GameBoard.
     * 
     * @param size El tamaño del tablero.
     */
    public GameBoard(int size) {
        this.size = size;
        this.isPlayable = false;
        this.listeners = new ArrayList<>();
        this.lastErrorCell = null;
        
        // Ajustar el tamaño de las celdas para tableros más grandes
        this.cellSize = Math.min(60, 600 / size); // Ajuste dinámico del tamaño de celda
        
        initializeComponents();
        layoutComponents();
        setupStyle();
    }

    /**
     * Inicializa los componentes del tablero.
     */
    private void initializeComponents() {
        cellPanels = new JPanel[size][size];
        cellButtons = new JButton[size][size];
        rightLabels = new JLabel[size][size];
        bottomLabels = new JLabel[size][size];

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                // Crear panel para cada celda
                cellPanels[row][col] = new JPanel(new BorderLayout(2, 2));
                
                // Crear botón de celda
                cellButtons[row][col] = createCellButton(row, col);
                
                // Crear labels para desigualdades
                rightLabels[row][col] = createInequalityLabel();
                bottomLabels[row][col] = createInequalityLabel();
                
                // Agregar componentes al panel
                cellPanels[row][col].add(cellButtons[row][col], BorderLayout.CENTER);
                
                if (col < size - 1) {
                    cellPanels[row][col].add(rightLabels[row][col], BorderLayout.EAST);
                }
                if (row < size - 1) {
                    cellPanels[row][col].add(bottomLabels[row][col], BorderLayout.SOUTH);
                }
            }
        }
    }

    /**
     * Crea un botón para una celda específica.
     * 
     * @param row La fila de la celda.
     * @param col La columna de la celda.
     * @return El botón creado.
     */
    private JButton createCellButton(final int row, final int col) {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(cellSize, cellSize));
        button.setFont(new Font("Arial", Font.PLAIN, 20));
        button.setFocusPainted(false);
        button.setBackground(CELL_BACKGROUND);
        button.setBorder(createCellBorder());
        
        button.addActionListener(e -> {
            if (isPlayable) {
                notifyCellClick(row, col);
            }
        });
        
        // Efecto hover
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (isPlayable && !button.getFont().isBold()) {
                    button.setBackground(new Color(240, 240, 240));
                }
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                if (!button.getFont().isBold()) {
                    button.setBackground(CELL_BACKGROUND);
                }
            }
        });
        
        return button;
    }

    /**
     * Crea un label para desigualdades.
     * 
     * @return El label creado.
     */
    private JLabel createInequalityLabel() {
        JLabel label = new JLabel(" ");
        label.setPreferredSize(new Dimension(INEQUALITY_SIZE, INEQUALITY_SIZE));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
        label.setFont(new Font("Arial Unicode MS", Font.BOLD, 20));
        return label;
    }

    /**
     * Crea el borde de una celda.
     * 
     * @return El borde creado.
     */
    private Border createCellBorder() {
        return BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(CELL_BORDER, 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        );
    }

    /**
     * Organiza los componentes del tablero.
     */
    private void layoutComponents() {
        setLayout(new GridLayout(size, size, 1, 1));
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        setBackground(BACKGROUND_COLOR);
        
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                add(cellPanels[i][j]);
            }
        }
    }

    /**
     * Configura el estilo del tablero.
     */
    private void setupStyle() {
        setPreferredSize(new Dimension(
            (cellSize + INEQUALITY_SIZE) * size,
            (cellSize + INEQUALITY_SIZE) * size
        ));
    }

    /**
     * Actualiza el tablero con los valores del modelo FutoshikiBoard.
     * 
     * @param board El modelo FutoshikiBoard.
     */
    public void updateBoard(FutoshikiBoard board) {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                Celda celda = board.getCellAt(row, col);
                updateCell(row, col, celda);
            }
        }
    }

    /**
     * Actualiza una celda específica con los valores del modelo Celda.
     * 
     * @param row La fila de la celda.
     * @param col La columna de la celda.
     * @param celda El modelo Celda.
     */
    public void updateCell(int row, int col, Celda celda) {
        // Actualizar valor
        JButton button = cellButtons[row][col];
        int valor = celda.getValor();
        button.setText(valor > 0 ? String.valueOf(valor) : "");
        
        // Actualizar estilo según si es constante
        if (celda.isConstant()) {
            button.setFont(new Font("Arial", Font.BOLD, 20));
            button.setForeground(CONSTANT_COLOR);
            button.setEnabled(false);
        } else {
            button.setFont(new Font("Arial", Font.PLAIN, 20));
            button.setForeground(Color.BLACK);
            button.setEnabled(isPlayable);
        }
        
        // Actualizar desigualdades
        if (col < size - 1) {
            updateInequalityLabel(rightLabels[row][col], celda.getDesDer());
        }
        if (row < size - 1) {
            updateInequalityLabel(bottomLabels[row][col], celda.getDesAbajo());
        }
        
        // Limpiar error si existe
        if (lastErrorCell != null && lastErrorCell.x == row && lastErrorCell.y == col) {
            button.setBackground(CELL_BACKGROUND);
            lastErrorCell = null;
        }
    }

    /**
     * Actualiza el label de desigualdad con el símbolo correspondiente.
     * 
     * @param label El label a actualizar.
     * @param inequality El símbolo de desigualdad.
     */
    private void updateInequalityLabel(JLabel label, String inequality) {
        switch (inequality) {
            case GameConstants.SYMBOL_LESSER:  // "<"
                label.setText(GameConstants.SYMBOL_LESSER);
                break;
            case GameConstants.SYMBOL_GREATER: // ">"
                label.setText(GameConstants.SYMBOL_GREATER);
                break;
            case GameConstants.SYMBOL_LESSER_COL: // "^"
                label.setText(GameConstants.SYMBOL_LESSER_COL);
                break;
            case GameConstants.SYMBOL_GREATER_COL: // "v"
                label.setText(GameConstants.SYMBOL_GREATER_COL);
                break;
            default:
                label.setText(" ");
        }
        
        // Configurar el estilo visual
        label.setFont(new Font("Arial Unicode MS", Font.BOLD, 20));
        label.setForeground(new Color(0, 102, 204)); // Azul oscuro para mejor visibilidad
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
    }

    /**
     * Muestra un error en una celda específica.
     * 
     * @param row La fila de la celda.
     * @param col La columna de la celda.
     */
    public void showError(int row, int col) {
        if (lastErrorCell != null) {
            cellButtons[lastErrorCell.x][lastErrorCell.y].setBackground(CELL_BACKGROUND);
        }
        
        cellButtons[row][col].setBackground(ERROR_COLOR);
        lastErrorCell = new Point(row, col);
        
        // Programar la limpieza del error después de 2 segundos
        Timer timer = new Timer(2000, e -> {
            if (lastErrorCell != null && lastErrorCell.x == row && lastErrorCell.y == col) {
                cellButtons[row][col].setBackground(CELL_BACKGROUND);
                lastErrorCell = null;
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    /**
     * Establece el tamaño del tablero.
     * 
     * @param newSize El nuevo tamaño del tablero.
     */
    public void setSize(int newSize) {
        if (this.size != newSize) {
            this.size = newSize;
            removeAll();
            initializeComponents();
            layoutComponents();
            setupStyle();
            revalidate();
            repaint();
        }
    }

    /**
     * Establece si el tablero es jugable.
     * 
     * @param playable true si el tablero es jugable, false en caso contrario.
     */
    public void setPlayable(boolean playable) {
        this.isPlayable = playable;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (!cellButtons[i][j].getFont().isBold()) {  // No es constante
                    cellButtons[i][j].setEnabled(playable);
                }
            }
        }
    }

    /**
     * Añade un listener para los clicks en las celdas.
     * 
     * @param listener El listener a añadir.
     */
    public void addCellClickListener(CellClickListener listener) {
        listeners.add(listener);
    }

    /**
     * Elimina un listener para los clicks en las celdas.
     * 
     * @param listener El listener a eliminar.
     */
    public void removeCellClickListener(CellClickListener listener) {
        listeners.remove(listener);
    }

    /**
     * Notifica a los listeners que una celda ha sido clickeada.
     * 
     * @param row La fila de la celda clickeada.
     * @param col La columna de la celda clickeada.
     */
    private void notifyCellClick(int row, int col) {
        for (CellClickListener listener : listeners) {
            listener.onCellClick(row, col);
        }
    }

    /**
     * Interfaz para el listener de clicks en celdas.
     */
    public interface CellClickListener {
        void onCellClick(int row, int col);
    }

    /**
     * Reinicia el tablero a su estado inicial.
     */
    public void reset() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                cellButtons[i][j].setText("");
                cellButtons[i][j].setBackground(CELL_BACKGROUND);
                cellButtons[i][j].setFont(new Font("Arial", Font.PLAIN, 20));
                cellButtons[i][j].setForeground(Color.BLACK);
                cellButtons[i][j].setEnabled(true);
                rightLabels[i][j].setText(" ");
                bottomLabels[i][j].setText(" ");
            }
        }
        lastErrorCell = null;
    }
}