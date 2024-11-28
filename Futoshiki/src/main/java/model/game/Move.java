package model.game;

public class Move {
    private int row;
    private int column;
    private int value;
    private int previousValue;

    /**
     * Constructor de la clase Move.
     * 
     * @param row La fila del movimiento.
     * @param column La columna del movimiento.
     * @param value El valor del movimiento.
     * @param previousValue El valor anterior del movimiento.
     */
    public Move(int row, int column, int value, int previousValue) {
        this.row = row;
        this.column = column;
        this.value = value;
        this.previousValue = previousValue;
    }

    // Métodos para deshacer/rehacer movimientos

    /**
     * Obtiene la fila del movimiento.
     * 
     * @return La fila del movimiento.
     */
    public int getRow() {
        return row;
    }

    /**
     * Obtiene la columna del movimiento.
     * 
     * @return La columna del movimiento.
     */
    public int getColumn() {
        return column;
    }

    /**
     * Obtiene el valor del movimiento.
     * 
     * @return El valor del movimiento.
     */
    public int getValue() {
        return value;
    }

    /**
     * Obtiene el valor anterior del movimiento.
     * 
     * @return El valor anterior del movimiento.
     */
    public int getPreviousValue() {
        return previousValue;
    }
}