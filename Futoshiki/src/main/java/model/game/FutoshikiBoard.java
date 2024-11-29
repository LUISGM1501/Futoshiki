package model.game;

import java.util.ArrayList;
import java.util.List;

import util.constants.GameConstants;

public class FutoshikiBoard {
    private int size;
    private Celda[][] cells;
    private List<Inequality> inequalities;
    private List<GameConstants> constants;

    /**
     * Constructor de la clase FutoshikiBoard.
     * 
     * @param size El tamaño del tablero.
     */
    public FutoshikiBoard(int size) {
        this.size = size;
        this.cells = new Celda[size][size];
        this.inequalities = new ArrayList<>();
        this.constants = new ArrayList<>();
        
        // Inicializar celdas
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                cells[i][j] = new Celda(0);
            }
        }
    }

    /**
     * Establece las celdas del tablero.
     * 
     * @param cells Las celdas a establecer.
     */
    public void setCells(Celda[][] cells) {
        this.cells = cells;
    }

    /**
     * Obtiene las celdas del tablero.
     * 
     * @return Las celdas del tablero.
     */
    public Celda[][] getCells() {
        return cells;
    }

    /**
     * Obtiene la celda en una posición específica.
     * 
     * @param row La fila de la celda.
     * @param col La columna de la celda.
     * @return La celda en la posición especificada.
     */
    public Celda getCellAt(int row, int col) {
        return cells[row][col];
    }

    /**
     * Obtiene el tamaño del tablero.
     * 
     * @return El tamaño del tablero.
     */
    public int getSize() {
        return size;
    }

    /**
     * Establece el valor de una celda en una posición específica.
     * 
     * @param row La fila de la celda.
     * @param col La columna de la celda.
     * @param value El valor a establecer.
     * @return true si el valor se estableció correctamente, false en caso contrario.
     */
    public boolean setCellValue(int row, int col, int value) {
        if (row < 0 || row >= size || col < 0 || col >= size) {
            return false;
        }

        if (cells[row][col].isConstant()) {
            return false;
        }

        // Validar rango del valor
        if (value < 1 || value > size) {
            return false;
        }

        // Validar fila
        for (int i = 0; i < size; i++) {
            if (i != col && cells[row][i].getValor() == value) {
                cells[row][col].setValor(value); // Temporalmente para mostrar el error
                return false;
            }
        }

        // Validar columna
        for (int i = 0; i < size; i++) {
            if (i != row && cells[i][col].getValor() == value) {
                cells[row][col].setValor(value); // Temporalmente para mostrar el error
                return false;
            }
        }

        // Validar desigualdades
        cells[row][col].setValor(value);
        if (!validateInequalities(row, col)) {
            cells[row][col].setValor(0); // Revertir si no es válido
            return false;
        }

        return true;
    }

    /**
     * Valida las desigualdades en una posición específica.
     * 
     * @param row La fila de la celda.
     * @param col La columna de la celda.
     * @return true si las desigualdades son válidas, false en caso contrario.
     */
    private boolean validateInequalities(int row, int col) {
        // Validar con celda a la derecha
        if (col < size - 1) {
            if (!validateInequalityRight(row, col)) {
                return false;
            }
        }

        // Validar con celda a la izquierda
        if (col > 0) {
            if (!validateInequalityLeft(row, col)) {
                return false;
            }
        }

        // Validar con celda abajo
        if (row < size - 1) {
            if (!validateInequalityBottom(row, col)) {
                return false;
            }
        }

        // Validar con celda arriba
        if (row > 0) {
            if (!validateInequalityTop(row, col)) {
                return false;
            }
        }

        return true;
    }

    private boolean validateInequalityRight(int row, int col) {
        String inequality = cells[row][col].getDesDer();
        int rightValue = cells[row][col + 1].getValor();
        
        if (rightValue == 0) return true;
        
        if (inequality.equals(">") && cells[row][col].getValor() <= rightValue) {
            return false;
        }
        if (inequality.equals("<") && cells[row][col].getValor() >= rightValue) {
            return false;
        }
        return true;
    }

    private boolean validateInequalityLeft(int row, int col) {
        String inequality = cells[row][col - 1].getDesDer();
        int leftValue = cells[row][col - 1].getValor();
        
        if (leftValue == 0) return true;
        
        if (inequality.equals(">") && leftValue <= cells[row][col].getValor()) {
            return false;
        }
        if (inequality.equals("<") && leftValue >= cells[row][col].getValor()) {
            return false;
        }
        return true;
    }

    private boolean validateInequalityBottom(int row, int col) {
        String inequality = cells[row][col].getDesAbajo();
        int bottomValue = cells[row + 1][col].getValor();
        
        if (bottomValue == 0) return true;
        
        if (inequality.equals("v") && cells[row][col].getValor() <= bottomValue) {
            return false;
        }
        if (inequality.equals("^") && cells[row][col].getValor() >= bottomValue) {
            return false;
        }
        return true;
    }

    private boolean validateInequalityTop(int row, int col) {
        String inequality = cells[row - 1][col].getDesAbajo();
        int topValue = cells[row - 1][col].getValor();
        
        if (topValue == 0) return true;
        
        if (inequality.equals("v") && topValue <= cells[row][col].getValor()) {
            return false;
        }
        if (inequality.equals("^") && topValue >= cells[row][col].getValor()) {
            return false;
        }
        return true;
    }

    /**
     * Verifica si el tablero está completo.
     * 
     * @return true si el tablero está completo, false en caso contrario.
     */
    public boolean isBoardComplete() {
        // Verificar que todas las celdas estén llenas
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (cells[i][j].getValor() == 0) {
                    return false;
                }
            }
        }

        // Verificar todas las desigualdades
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (!validateInequalities(i, j)) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Establece una celda como constante.
     * 
     * @param row La fila de la celda.
     * @param col La columna de la celda.
     * @param value El valor a establecer.
     */
    public void setConstant(int row, int col, int value) {
        cells[row][col].setValor(value);
        cells[row][col].setConstant(true);
    }

    /**
     * Establece una desigualdad en una posición específica.
     * 
     * @param type El tipo de desigualdad.
     * @param row La fila de la celda.
     * @param col La columna de la celda.
     */
    public void setInequality(String type, int row, int col) {
        switch (type) {
            case "maf": // mayor que en fila
                cells[row][col].creacionDesigualdades(2); // Para >
                break;
            case "mef": // menor que en fila
                cells[row][col].creacionDesigualdades(1); // Para <
                break;
            case "mac": // mayor que en columna
                cells[row][col].creacionDesigualdades(4); // Para v
                break;
            case "mec": // menor que en columna
                cells[row][col].creacionDesigualdades(3); // Para ^
                break;
        }
    }

    /**
     * Verifica si una celda es constante.
     * 
     * @param row La fila de la celda.
     * @param col La columna de la celda.
     * @return true si la celda es constante, false en caso contrario.
     */
    public boolean isConstant(int row, int col) {
        return cells[row][col].isConstant();
    }

    /**
     * Obtiene el valor de una celda en una posición específica.
     * 
     * @param row La fila de la celda.
     * @param col La columna de la celda.
     * @return El valor de la celda.
     */
    public int getValue(int row, int col) {
        return cells[row][col].getValor();
    }

    /**
     * Obtiene la desigualdad hacia la derecha de una celda en una posición específica.
     * 
     * @param row La fila de la celda.
     * @param col La columna de la celda.
     * @return La desigualdad hacia la derecha.
     */
    public String getRightInequality(int row, int col) {
        return cells[row][col].getDesDer();
    }

    /**
     * Obtiene la desigualdad hacia abajo de una celda en una posición específica.
     * 
     * @param row La fila de la celda.
     * @param col La columna de la celda.
     * @return La desigualdad hacia abajo.
     */
    public String getBottomInequality(int row, int col) {
        return cells[row][col].getDesAbajo();
    }

    /**
     * Limpia el valor de una celda en una posición específica si no es constante.
     * 
     * @param row La fila de la celda.
     * @param col La columna de la celda.
     * @return true si la celda se limpió correctamente, false en caso contrario.
     */
    public boolean clearCell(int row, int col) {
        if (!cells[row][col].isConstant()) {
            cells[row][col].setValor(0);
            return true;
        }
        return false;
    }

    /**
     * Limpia todas las celdas no constantes del tablero.
     */
    public void clearNonConstantCells() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (!cells[i][j].isConstant()) {
                    cells[i][j].setValor(0);
                }
            }
        }
    }

    /**
     * Devuelve una representación en cadena del tablero.
     * 
     * @return Una cadena que representa el tablero.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                sb.append(cells[i][j].getValor()).append(" ");
                if (j < size - 1) {
                    sb.append(cells[i][j].getDesDer()).append(" ");
                }
            }
            sb.append("\n");
            if (i < size - 1) {
                for (int j = 0; j < size; j++) {
                    sb.append(cells[i][j].getDesAbajo()).append("   ");
                }
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    /**
     * Resuelve el tablero utilizando el algoritmo de backtracking.
     * 
     * @return true si el tablero se resolvió correctamente, false en caso contrario.
     */
    public boolean solve() {
        return solveBacktracking(0, 0);
    }

    /**
     * Resuelve el tablero utilizando el algoritmo de backtracking.
     * 
     * @param row La fila actual.
     * @param col La columna actual.
     * @return true si el tablero se resolvió correctamente, false en caso contrario.
     */
    private boolean solveBacktracking(int row, int col) {
        // Si llegamos al final del tablero, está resuelto
        if (row == size) {
            return true;
        }

        // Calcular siguiente posición
        int nextRow = (col == size - 1) ? row + 1 : row;
        int nextCol = (col == size - 1) ? 0 : col + 1;

        // Si la celda es constante, pasar a la siguiente
        if (cells[row][col].isConstant()) {
            return solveBacktracking(nextRow, nextCol);
        }

        // Probar cada valor posible
        for (int num = 1; num <= size; num++) {
            // Guardar valor actual para poder restaurarlo
            int currentValue = cells[row][col].getValor();
            
            if (isValidMove(row, col, num)) {
                cells[row][col].setValor(num);
                
                if (solveBacktracking(nextRow, nextCol)) {
                    return true;
                }
            }
            
            // Restaurar valor si no funcionó
            cells[row][col].setValor(currentValue);
        }

        return false;
    }

    /**
     * Verifica si un movimiento es válido en una posición específica.
     * 
     * @param row La fila de la celda.
     * @param col La columna de la celda.
     * @param value El valor a verificar.
     * @return true si el movimiento es válido, false en caso contrario.
     */
    private boolean isValidMove(int row, int col, int value) {
        // Validar fila
        for (int i = 0; i < size; i++) {
            if (i != col && cells[row][i].getValor() == value) {
                return false;
            }
        }

        // Validar columna
        for (int i = 0; i < size; i++) {
            if (i != row && cells[i][col].getValor() == value) {
                return false;
            }
        }

        // Validar desigualdades
        // Con celda a la derecha
        if (col < size - 1) {
            int rightValue = cells[row][col + 1].getValor();
            if (rightValue != 0) {
                String inequality = cells[row][col].getDesDer();
                if (inequality.equals(">") && value <= rightValue) return false;
                if (inequality.equals("<") && value >= rightValue) return false;
            }
        }

        // Con celda a la izquierda
        if (col > 0) {
            int leftValue = cells[row][col - 1].getValor();
            if (leftValue != 0) {
                String inequality = cells[row][col - 1].getDesDer();
                if (inequality.equals(">") && leftValue <= value) return false;
                if (inequality.equals("<") && leftValue >= value) return false;
            }
        }

        // Con celda abajo
        if (row < size - 1) {
            int bottomValue = cells[row + 1][col].getValor();
            if (bottomValue != 0) {
                String inequality = cells[row][col].getDesAbajo();
                if (inequality.equals("v") && value <= bottomValue) return false;
                if (inequality.equals("^") && value >= bottomValue) return false;
            }
        }

        // Con celda arriba
        if (row > 0) {
            int topValue = cells[row - 1][col].getValor();
            if (topValue != 0) {
                String inequality = cells[row - 1][col].getDesAbajo();
                if (inequality.equals("v") && topValue <= value) return false;
                if (inequality.equals("^") && topValue >= value) return false;
            }
        }

        return true;
    }
}