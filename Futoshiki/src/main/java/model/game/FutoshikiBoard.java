package model.game;

import java.util.ArrayList;
import java.util.List;

import util.constants.GameConstants;

public class FutoshikiBoard {
    private int size;
    private Celda[][] cells;
    private List<Inequality> inequalities;
    private List<GameConstants> constants;

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

    public void setCells(Celda[][] cells) {
        this.cells = cells;
    }

    public Celda[][] getCells() {
        return cells;
    }

    public Celda getCellAt(int row, int col) {
        return cells[row][col];
    }

    public int getSize() {
        return size;
    }

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
        cells[row][col].setValor(value);
        boolean isValid = validateInequalities(row, col);
        
        if (!isValid) {
            cells[row][col].setValor(0); // Revertir si no es válido
            return false;
        }

        return true;
    }

    private boolean validateInequalities(int row, int col) {
        // Validar con celda a la derecha
        if (col < size - 1) {
            if (!cells[row][col].revisarDesigualdadesDer(cells[row][col + 1])) {
                return false;
            }
        }

        // Validar con celda a la izquierda
        if (col > 0) {
            if (!cells[row][col - 1].revisarDesigualdadesDer(cells[row][col])) {
                return false;
            }
        }

        // Validar con celda abajo
        if (row < size - 1) {
            if (!cells[row][col].revisarDesigualdadesAba(cells[row + 1][col])) {
                return false;
            }
        }

        // Validar con celda arriba
        if (row > 0) {
            if (!cells[row - 1][col].revisarDesigualdadesAba(cells[row][col])) {
                return false;
            }
        }

        return true;
    }

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

    public void setConstant(int row, int col, int value) {
        cells[row][col].setValor(value);
        cells[row][col].setConstant(true);
    }

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

    public boolean isConstant(int row, int col) {
        return cells[row][col].isConstant();
    }

    public int getValue(int row, int col) {
        return cells[row][col].getValor();
    }

    public String getRightInequality(int row, int col) {
        return cells[row][col].getDesDer();
    }

    public String getBottomInequality(int row, int col) {
        return cells[row][col].getDesAbajo();
    }

    public boolean clearCell(int row, int col) {
        if (!cells[row][col].isConstant()) {
            cells[row][col].setValor(0);
            return true;
        }
        return false;
    }

    public void clearNonConstantCells() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (!cells[i][j].isConstant()) {
                    cells[i][j].setValor(0);
                }
            }
        }
    }

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
}