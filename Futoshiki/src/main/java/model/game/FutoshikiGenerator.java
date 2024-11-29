package model.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FutoshikiGenerator {
    private static final Random random = new Random();

    /**
     * Genera una nueva partida de Futoshiki aleatoria.
     * 
     * @param size Tamaño del tablero (6-10)
     * @param difficulty Dificultad ("Facil", "Intermedio", "Dificil")
     * @return FutoshikiBoard con el juego generado
     */
    public static FutoshikiBoard generateGame(int size, String difficulty) {
        System.out.println("FutoshikiGenerator: Generando juego " + size + "x" + size + " dificultad " + difficulty);

        // 1. Crear tablero vacío
        FutoshikiBoard board = new FutoshikiBoard(size);
        System.out.println("FutoshikiGenerator: Tablero creado");
        
        // 2. Generar una solución válida completa
        int[][] solution = generateValidSolution(size);
        System.out.println("FutoshikiGenerator: Solución generada");
        
        // NUEVO: Imprimir la solución para verificación
        System.out.println("Solución completa generada:");
        for (int i = 0; i < size; i++) {
            StringBuilder row = new StringBuilder();
            for (int j = 0; j < size; j++) {
                row.append(solution[i][j]).append(" ");
            }
            System.out.println(row.toString());
        }
        
        // Verificar que la solución es válida
        boolean solucionValida = validarSolucion(solution, size);
        System.out.println("La solución generada es válida: " + solucionValida);
        
        // 3. Determinar número de pistas según dificultad
        int numConstants = calculateConstants(size, difficulty);
        int numInequalities = calculateInequalities(size, difficulty);
        System.out.println("FutoshikiGenerator: Número de constantes y desigualdades calculado");
        
        // 4. Colocar constantes aleatorias de la solución
        placeRandomConstants(board, solution, numConstants);
        System.out.println("FutoshikiGenerator: Constantes colocadas");
        
        // 5. Colocar desigualdades válidas
        placeRandomInequalities(board, solution, numInequalities);
        System.out.println("FutoshikiGenerator: Desigualdades colocadas");
        
        return board;
    }

    /**
     * Genera una solución válida completa.
     */
    private static int[][] generateValidSolution(int size) {
        int[][] board = new int[size][size];
        if (solveRandomly(board, 0, 0, size)) {
            return board;
        }
        throw new RuntimeException("No se pudo generar una solución válida");
    }

    /**
     * Resuelve el tablero de manera aleatoria usando backtracking.
     */
    private static boolean solveRandomly(int[][] board, int row, int col, int size) {
        if (col == size) {
            col = 0;
            row++;
        }
        if (row == size) {
            return true;
        }

        // Crear lista de números aleatorios del 1 al size
        List<Integer> numbers = new ArrayList<>();
        for (int i = 1; i <= size; i++) {
            numbers.add(i);
        }
        // Mezclar números
        for (int i = numbers.size() - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int temp = numbers.get(i);
            numbers.set(i, numbers.get(j));
            numbers.set(j, temp);
        }

        // Probar cada número en orden aleatorio
        for (int num : numbers) {
            if (isValidMove(board, row, col, num, size)) {
                board[row][col] = num;
                if (solveRandomly(board, row, col + 1, size)) {
                    return true;
                }
                board[row][col] = 0;
            }
        }
        return false;
    }

    /**
     * Verifica si un movimiento es válido.
     */
    private static boolean isValidMove(int[][] board, int row, int col, int num, int size) {
        // Verificar fila
        for (int x = 0; x < size; x++) {
            if (board[row][x] == num) return false;
        }
        
        // Verificar columna
        for (int x = 0; x < size; x++) {
            if (board[x][col] == num) return false;
        }
        
        return true;
    }

    /**
     * Calcula el número de constantes según dificultad.
     */
    private static int calculateConstants(int size, String difficulty) {
        double factor;
        switch (difficulty) {
            case "Facil":
                factor = 0.3; // Ajustado a 30% para dar más pistas
                break;
            case "Intermedio":
                factor = 0.25;
                break;
            case "Dificil":
                factor = 0.2;
                break;
            default:
                factor = 0.25;
        }
        int constants = (int)(size * size * factor);
        System.out.println("Calculando constantes para dificultad " + difficulty + 
                          ": " + constants + " constantes");
        return constants;
    }

    /**
     * Calcula el número de desigualdades según dificultad.
     */
    private static int calculateInequalities(int size, String difficulty) {
        double factor;
        switch (difficulty) {
            case "Facil":
                factor = 0.2; // Ajustado para dar más restricciones
                break;
            case "Intermedio":
                factor = 0.3;
                break;
            case "Dificil":
                factor = 0.4;
                break;
            default:
                factor = 0.3;
        }
        // Máximo de desigualdades posibles: horizontal + vertical
        int maxInequalities = 2 * (size * (size-1));
        int inequalities = (int)(maxInequalities * factor);
        System.out.println("Calculando desigualdades para dificultad " + difficulty + 
                          ": " + inequalities + " desigualdades");
        return inequalities;
    }

    /**
     * Coloca constantes aleatorias de la solución.
     */
    private static void placeRandomConstants(FutoshikiBoard board, int[][] solution, int numConstants) {
        System.out.println("Colocando " + numConstants + " constantes...");
        int placed = 0;
        int size = board.getSize();
        
        while (placed < numConstants) {
            int row = random.nextInt(size);
            int col = random.nextInt(size);
            
            if (board.getValue(row, col) == 0) {
                board.setConstant(row, col, solution[row][col]);
                System.out.println("Constante colocada en [" + row + "," + col + "] = " + solution[row][col]);
                placed++;
            }
        }
    }

    /**
     * Coloca desigualdades válidas basadas en la solución.
     */
    private static void placeRandomInequalities(FutoshikiBoard board, int[][] solution, int numInequalities) {
        System.out.println("Colocando " + numInequalities + " desigualdades...");
        int placed = 0;
        int size = board.getSize();
        
        while (placed < numInequalities && placed < size * size) {
            // Decidir si poner desigualdad horizontal o vertical
            boolean isHorizontal = random.nextBoolean();
            
            if (isHorizontal) {
                int row = random.nextInt(size);
                int col = random.nextInt(size - 1);
                
                if (solution[row][col] > solution[row][col + 1]) {
                    board.setInequality("maf", row, col); // mayor que
                    System.out.println("Desigualdad horizontal > en [" + row + "," + col + "]");
                    placed++;
                } else if (solution[row][col] < solution[row][col + 1]) {
                    board.setInequality("mef", row, col); // menor que
                    System.out.println("Desigualdad horizontal < en [" + row + "," + col + "]");
                    placed++;
                }
            } else {
                int row = random.nextInt(size - 1);
                int col = random.nextInt(size);
                
                if (solution[row][col] > solution[row + 1][col]) {
                    board.setInequality("mac", row, col); // mayor que vertical
                    System.out.println("Desigualdad vertical v en [" + row + "," + col + "]");
                    placed++;
                } else if (solution[row][col] < solution[row + 1][col]) {
                    board.setInequality("mec", row, col); // menor que vertical
                    System.out.println("Desigualdad vertical ^ en [" + row + "," + col + "]");
                    placed++;
                }
            }
        }
    }

    /**
     * Verifica que la solución generada es válida.
     */
    private static boolean validarSolucion(int[][] solution, int size) {
        // Verificar filas y columnas
        for (int i = 0; i < size; i++) {
            boolean[] usadosFila = new boolean[size + 1];
            boolean[] usadosColumna = new boolean[size + 1];
            for (int j = 0; j < size; j++) {
                // Verificar fila
                if (solution[i][j] != 0) {
                    if (usadosFila[solution[i][j]]) return false;
                    usadosFila[solution[i][j]] = true;
                }
                // Verificar columna
                if (solution[j][i] != 0) {
                    if (usadosColumna[solution[j][i]]) return false;
                    usadosColumna[solution[j][i]] = true;
                }
            }
        }
        return true;
    }
}