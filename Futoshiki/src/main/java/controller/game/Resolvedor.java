package controller.game;
import java.util.*;

public class Resolvedor {
    private int[][] tabla;
    private String[][] desVer;
    private String[][] desHor;
    private int size;

    public Resolvedor(int[][] board, String[][] desVer, String[][] desHor) {
        this.tabla = board;
        this.desVer = desVer;
        this.desHor = desHor;
        this.size = board.length;
    }


    public boolean isSolvable() {
        return solve(0, 0);
    }


    private boolean solve(int row, int col) {
        if (row == size) return true;

        int nextRow = col == size - 1 ? row + 1 : row;
        int nextCol = (col + 1) % size;

        // If cell already has a value, move to the next cell
        if (tabla[row][col] != 0) {
            return solve(nextRow, nextCol);
        }

        // Try each number
        for (int num = 1; num <= size; num++) {
            if (isSafe(row, col, num)) {
                tabla[row][col] = num;
                if (solve(nextRow, nextCol)) return true;

                // Backtrack
                tabla[row][col] = 0;
            }
        }

        return false;
    }

    private boolean isSafe(int row, int col, int num) {
        // Check row and column uniqueness
        for (int i = 0; i < size; i++) {
            if (tabla[row][i] == num || tabla[i][col] == num) return false;
        }

        // Check vertical constraints
        if (row > 0) {
            String constraint = desVer[row - 1][col];
            if (constraint != null && !constraint.equals(" ")) {
                if (constraint.equals("mef") && !(tabla[row - 1][col] < num)) return false;
                if (constraint.equals("maf") && !(tabla[row - 1][col] > num)) return false;
            }
        }

        // Check horizontal constraints
        if (col > 0) {
            String constraint = desHor[row][col - 1];
            if (constraint != null && !constraint.equals(" ")) {
                if (constraint.equals("mec") && !(tabla[row][col - 1] < num)) return false;
                if (constraint.equals("mac") && !(tabla[row][col - 1] > num)) return false;
            }
        }

        return true;
    }

}
