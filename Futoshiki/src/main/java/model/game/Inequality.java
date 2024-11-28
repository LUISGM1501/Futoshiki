package model.game;

public class Inequality {
    private int row1, col1;  // Primera casilla
    private int row2, col2;  // Segunda casilla
    private String type;     // "maf", "mef", "mac", "mec"GameState.java
    
    /**
     * Constructor de la clase Inequality.
     * 
     * @param type El tipo de desigualdad ("maf", "mef", "mac", "mec").
     * @param row La fila de la primera casilla.
     * @param column La columna de la primera casilla.
     */
    public Inequality(String type, int row, int column) {
        this.type = type;
        this.row1 = row;
        this.col1 = column;
        
        // Set second cell based on type
        if (type.equals("maf") || type.equals("mef")) {
            // Horizontal inequality
            this.row2 = row;
            this.col2 = column + 1;
        } else {
            // Vertical inequality 
            this.row2 = row + 1;
            this.col2 = column;
        }
    }

    /**
     * Obtiene la fila de la primera casilla.
     * 
     * @return La fila de la primera casilla.
     */
    public int getRow1() {
        return row1;
    }

    /**
     * Obtiene la columna de la primera casilla.
     * 
     * @return La columna de la primera casilla.
     */
    public int getCol1() {
        return col1;
    }

    /**
     * Obtiene la fila de la segunda casilla.
     * 
     * @return La fila de la segunda casilla.
     */
    public int getRow2() {
        return row2;
    }

    /**
     * Obtiene la columna de la segunda casilla.
     * 
     * @return La columna de la segunda casilla.
     */
    public int getCol2() {
        return col2;
    }

    /**
     * Obtiene el tipo de desigualdad.
     * 
     * @return El tipo de desigualdad.
     */
    public String getType() {
        return type;
    }
}