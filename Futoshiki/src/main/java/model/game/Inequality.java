package model.game;

public class Inequality {
    private int row1, col1;  // Primera casilla
    private int row2, col2;  // Segunda casilla
    private String type;     // "maf", "mef", "mac", "mec"GameState.java
    
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

    public int getRow1() {
        return row1;
    }

    public int getCol1() {
        return col1;
    }

    public int getRow2() {
        return row2;
    }

    public int getCol2() {
        return col2;
    }

    public String getType() {
        return type;
    }

    
}