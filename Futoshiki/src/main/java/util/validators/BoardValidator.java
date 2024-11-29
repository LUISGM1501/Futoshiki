package util.validators;

import model.game.Celda;

public class BoardValidator {
    
    // Valida una jugada en el tablero
    /**
     * Valida una jugada en el tablero de Futoshiki.
     * 
     * @param board El tablero de juego representado como una matriz de celdas.
     * @param row La fila de la celda donde se realiza la jugada.
     * @param col La columna de la celda donde se realiza la jugada.
     * @param value El valor que se desea colocar en la celda.
     * @param size El tamaño del tablero.
     * @return Un mensaje de error si la jugada no es válida, o null si es válida.
     */
    public static String validateMove(Celda[][] board, int row, int col, int value, int size) {
        // Validar si la celda es constante
        if (board[row][col].isConstant()) {
            return "NO SE PUEDE MODIFICAR UNA CELDA CONSTANTE";
        }
        
        // Validar fila
        for (int i = 0; i < size; i++) {
            if (i != col && board[row][i].getValor() == value) {
                return "JUGADA NO ES VÁLIDA PORQUE EL ELEMENTO YA ESTÁ EN LA FILA";
            }
        }
        
        // Validar columna
        for (int i = 0; i < size; i++) {
            if (i != row && board[i][col].getValor() == value) {
                return "JUGADA NO ES VÁLIDA PORQUE EL ELEMENTO YA ESTÁ EN LA COLUMNA";
            }
        }
        
        // Validar desigualdades
        // Con celda a la derecha
        if (col < size - 1) {
            Celda celdaDerecha = board[row][col + 1];
            if (!board[row][col].revisarDesigualdadesDer(celdaDerecha)) {
                if (board[row][col].getDesDer().equals(">")) {
                    return "JUGADA NO ES VÁLIDA PORQUE NO CUMPLE CON LA RESTRICCIÓN DE MAYOR";
                } else {
                    return "JUGADA NO ES VÁLIDA PORQUE NO CUMPLE CON LA RESTRICCIÓN DE MENOR";
                }
            }
        }
        
        // Con celda a la izquierda
        if (col > 0) {
            if (!board[row][col-1].revisarDesigualdadesDer(board[row][col])) {
                if (board[row][col-1].getDesDer().equals(">")) {
                    return "JUGADA NO ES VÁLIDA PORQUE NO CUMPLE CON LA RESTRICCIÓN DE MAYOR";
                } else {
                    return "JUGADA NO ES VÁLIDA PORQUE NO CUMPLE CON LA RESTRICCIÓN DE MENOR";
                }
            }
        }
        
        // Con celda abajo
        if (row < size - 1) {
            if (!board[row][col].revisarDesigualdadesAba(board[row + 1][col])) {
                if (board[row][col].getDesAbajo().equals("v")) {
                    return "JUGADA NO ES VÁLIDA PORQUE NO CUMPLE CON LA RESTRICCIÓN DE MAYOR";
                } else {
                    return "JUGADA NO ES VÁLIDA PORQUE NO CUMPLE CON LA RESTRICCIÓN DE MENOR";
                }
            }
        }
        
        // Con celda arriba
        if (row > 0) {
            if (!board[row-1][col].revisarDesigualdadesAba(board[row][col])) {
                if (board[row-1][col].getDesAbajo().equals("v")) {
                    return "JUGADA NO ES VÁLIDA PORQUE NO CUMPLE CON LA RESTRICCIÓN DE MAYOR";
                } else {
                    return "JUGADA NO ES VÁLIDA PORQUE NO CUMPLE CON LA RESTRICCIÓN DE MENOR";
                }
            }
        }
        
        return null; // La jugada es válida
    }
    
    // Verifica si el tablero está completo y válido
    /**
     * Verifica si el tablero está completo y todas las jugadas son válidas.
     * 
     * @param board El tablero de juego representado como una matriz de celdas.
     * @param size El tamaño del tablero.
     * @return true si el tablero está completo y válido, false de lo contrario.
     */
    public static boolean isGameComplete(Celda[][] board, int size) {
        // Verificar que todas las celdas estén llenas
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j].getValor() == 0) {
                    return false;
                }
            }
        }
        
        // Si llegamos aquí, todas las celdas están llenas y válidas
        // (porque no se permiten jugadas inválidas)
        return true;
    }
}