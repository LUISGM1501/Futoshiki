package model.game;

import util.constants.GameConstants;

public class Celda {
    private int valor;
    private String desDer;
    private String desAbajo;
    private boolean isConstant; // Para indicar si es un número constante

    // Constructor
    /**
     * Constructor de la clase Celda.
     * 
     * @param valor El valor inicial de la celda.
     */
    public Celda(int valor) {
        this.valor = valor;
        this.desDer = " ";
        this.desAbajo = " ";
        this.isConstant = false;
    }

    // Métodos
    /**
     * Crea desigualdades en la celda basadas en la versión proporcionada.
     * 
     * @param version La versión que determina las desigualdades a aplicar.
     */
    public void creacionDesigualdades(int version) {
        switch(version) {
            case 1:
                desDer = GameConstants.SYMBOL_LESSER;  // "<"
                break;
            case 2:
                desDer = GameConstants.SYMBOL_GREATER; // ">"
                break;
            case 3:
                desAbajo = GameConstants.SYMBOL_LESSER_COL; // "^"
                break;
            case 4:
                desAbajo = GameConstants.SYMBOL_GREATER_COL; // "v"
                break;
            case 5:
                desDer = GameConstants.SYMBOL_LESSER;
                desAbajo = GameConstants.SYMBOL_LESSER_COL;
                break;
            case 6:
                desDer = GameConstants.SYMBOL_LESSER;
                desAbajo = GameConstants.SYMBOL_GREATER_COL;
                break;
            case 7:
                desDer = GameConstants.SYMBOL_GREATER;
                desAbajo = GameConstants.SYMBOL_LESSER_COL;
                break;
            case 8:
                desDer = GameConstants.SYMBOL_GREATER;
                desAbajo = GameConstants.SYMBOL_GREATER_COL;
                break;
            default:
                desDer = " ";
                desAbajo = " ";
        }
    }

    // Métodos existentes
    /**
     * Obtiene la desigualdad hacia abajo.
     * 
     * @return La desigualdad hacia abajo.
     */
    public String getDesAbajo() {
        return desAbajo;
    }

    /**
     * Obtiene la desigualdad hacia la derecha.
     * 
     * @return La desigualdad hacia la derecha.
     */
    public String getDesDer() {
        return desDer;
    }

    /**
     * Obtiene el valor de la celda.
     * 
     * @return El valor de la celda.
     */
    public int getValor() {
        return valor;
    }

    /**
     * Establece el valor de la celda si no es constante.
     * 
     * @param numero El nuevo valor a establecer.
     */
    public void setValor(int numero) {
        if (!isConstant) {
            this.valor = numero;
        }
    }

    /**
     * Verifica si la celda es constante.
     * 
     * @return true si la celda es constante, false en caso contrario.
     */
    public boolean isConstant() {
        return isConstant;
    }

    /**
     * Establece si la celda es constante.
     * 
     * @param constant true para hacer la celda constante, false en caso contrario.
     */
    public void setConstant(boolean constant) {
        isConstant = constant;
    }

    /**
     * Revisa las desigualdades hacia la derecha con otra celda.
     * 
     * @param celdaDerecha La celda a la derecha para comparar.
     * @return true si las desigualdades se cumplen, false en caso contrario.
     */
    public boolean revisarDesigualdadesDer(Celda celdaDerecha) {
        if(celdaDerecha.getValor() == 0) return true;
        if(desDer.equals(">") && valor < celdaDerecha.getValor()) return false;
        if(desDer.equals("<") && valor > celdaDerecha.getValor()) return false;
        return true;
    }

    /**
     * Revisa las desigualdades hacia abajo con otra celda.
     * 
     * @param celdaAbajo La celda abajo para comparar.
     * @return true si las desigualdades se cumplen, false en caso contrario.
     */
    public boolean revisarDesigualdadesAba(Celda celdaAbajo) {
        if(celdaAbajo.getValor() == 0) return true;
        if(desAbajo.equals("v") && valor < celdaAbajo.getValor()) return false;
        if(desAbajo.equals("^") && valor > celdaAbajo.getValor()) return false;
        return true;
    }

    /**
     * Revisa las desigualdades con las celdas adyacentes.
     * 
     * @param celdaAbajo La celda abajo para comparar.
     * @param celdaDerecha La celda a la derecha para comparar.
     * @return true si todas las desigualdades se cumplen, false en caso contrario.
     */
    public boolean revisarDesigualdades(Celda celdaAbajo, Celda celdaDerecha) {
        if(celdaDerecha.getValor() == 0 && celdaAbajo.getValor() == 0) return true;
        if(desAbajo.equals("v") && valor < celdaAbajo.getValor()) return false;
        if(desAbajo.equals("^") && valor > celdaAbajo.getValor()) return false;
        if(desDer.equals(">") && valor < celdaDerecha.getValor()) return false;
        if(desDer.equals("<") && valor > celdaDerecha.getValor()) return false;
        return true;
    }

    /**
     * Devuelve una representación en cadena de la celda.
     * 
     * @return Una cadena que representa el valor y la desigualdad hacia la derecha.
     */
    @Override
    public String toString() {
        return valor + " " + desDer;
    }
}