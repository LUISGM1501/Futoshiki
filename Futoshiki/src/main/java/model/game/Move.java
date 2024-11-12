package model.game;

public class Move {
    private int row;
    private int column;
    private int value;
    private int previousValue;

    public Move(int row, int column, int value, int previousValue) {
        this.row = row;
        this.column = column;
        this.value = value;
        this.previousValue = previousValue;
    }

    // Métodos para deshacer/rehacer movimientos
    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public int getValue() {
        return value;
    }

    public int getPreviousValue() {
        return previousValue;
    }
}