package controller.game;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RandomGames
{
    private int[][]tabla;
    private String[][] desigualdadesVerticales;
    private String[][] desigualdesdesHorizanteles;
    private int tamano;
    private Random rndNumber = new Random();

    public RandomGames(int tamano)
    {
        this.tamano = tamano;
        tabla = new int[tamano][tamano];
        desigualdadesVerticales = new String[tamano-1][tamano];
        desigualdesdesHorizanteles = new String[tamano][tamano-1];
    }


    public int[][] getTabla() {
        return tabla;
    }

    public String[][] getDesigualdadesVerticales() {
        return desigualdadesVerticales;
    }

    public String[][] getDesigualdesdesHorizanteles() {
        return desigualdesdesHorizanteles;
    }


    private boolean limpiarTabla(int row, int col)
    {
        if(row == tamano)
        {
            return true;
        }
        int nextRow = col == tamano - 1 ? row + 1 : row;
        int nextCol = (col + 1) % tamano;
        if (row > 0) desigualdadesVerticales[row - 1][col] = " ";
        if (col > 0) desigualdesdesHorizanteles[row][col - 1] = " ";
        tabla[row][col] = 0;

        return limpiarTabla(nextRow, nextCol);


    }

    public boolean generarJuegoAleatorio()
    {

        do
        {
            limpiarTabla(0,0);
            generacionAleatoria(0,0);
            generacionDesigualdades(0,0);

        }while(!desarmarTabla(0,0));

        System.out.println("Se logro!");
        return true;


    }

    private boolean solve()
    {
        Resolvedor solver = new Resolvedor(tabla, desigualdadesVerticales, desigualdesdesHorizanteles);
        return solver.isSolvable();
    }

    private boolean generacionDesigualdades(int row, int col)
    {
        if (row == tamano)
        {
            System.out.print("Nao se puede");
            return solve();
        }

        int nextRow = col == tamano - 1 ? row + 1 : row;
        int nextCol = (col + 1) % tamano;
        if (row > 0) desigualdadesVerticales[row - 1][col] = tabla[row - 1][col] < tabla[row][col] ? "mef" : "maf";
        if (col > 0) desigualdesdesHorizanteles[row][col - 1] = tabla[row][col - 1] < tabla[row][col] ? "mec" : "mac";

        return generacionDesigualdades(nextRow, nextCol);
    }

    private boolean generacionAleatoria(int row, int col) {
        if (row == tamano) {
            return true;
        }

        int nextRow = col == tamano - 1 ? row + 1 : row;
        int nextCol = (col + 1) % tamano;

        List<Integer> numbers = new ArrayList<>();
        for (int i = 1; i <= tamano; i++) numbers.add(i);
        Collections.shuffle(numbers);

        for (int num : numbers) {
            if (isSafe(row, col, num)) {
                tabla[row][col] = num;
                if (generacionAleatoria(nextRow, nextCol)) return true;
                tabla[row][col] = 0;
            }
        }
        return false;
    }



    private boolean isSafe(int row, int col, int num) {
        for (int i = 0; i < tamano; i++) {
            if (tabla[row][i] == num || tabla[i][col] == num) return false;
        }
        return true;
    }


    private boolean desarmarTabla(int row, int col)
    {
        if (row == tamano){ return true;}

        int nextRow = col == tamano - 1 ? row + 1 : row;
        int nextCol = (col + 1) % tamano;


        if (rndNumber.nextBoolean()) {
            tabla[row][col] = 0;
        }

        if (row > 0 && rndNumber.nextBoolean()) {
            desigualdadesVerticales[row - 1][col] = " ";
        }

        if (col > 0 && rndNumber.nextBoolean()) {
            desigualdesdesHorizanteles[row][col - 1] = " ";
        }

        return desarmarTabla(nextRow, nextCol);
    }
}