package model.game;

import util.constants.GameConstants;

public class Celda {
    private int valor;
    private String desDer;
    private String desAbajo;
    private boolean isConstant; // Para indicar si es un número constante

    // Constructor
    public Celda(int valor) {
        this.valor = valor;
        this.desDer = " ";
        this.desAbajo = " ";
        this.isConstant = false;
    }

    // Métodos
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
    public String getDesAbajo() {
        return desAbajo;
    }

    public String getDesDer() {
        return desDer;
    }

    public int getValor() {
        return valor;
    }

    public void setValor(int numero) {
        if (!isConstant) {
            this.valor = numero;
        }
    }

    public boolean isConstant() {
        return isConstant;
    }

    public void setConstant(boolean constant) {
        isConstant = constant;
    }

    public boolean revisarDesigualdadesDer(Celda celdaDerecha) {
        if(celdaDerecha.getValor() == 0) return true;

        if(desDer.equals(">") && valor < celdaDerecha.getValor()) return false;
        if(desDer.equals("<") && valor > celdaDerecha.getValor()) return false;
        return true;
    }

    public boolean revisarDesigualdadesAba(Celda celdaAbajo) {
        if(celdaAbajo.getValor() == 0) return true;
        if(desAbajo.equals("v") && valor < celdaAbajo.getValor()) return false;
        if(desAbajo.equals("^") && valor > celdaAbajo.getValor()) return false;
        return true;
    }

    public boolean revisarDesigualdades(Celda celdaAbajo, Celda celdaDerecha) {
        if(celdaDerecha.getValor() == 0 && celdaAbajo.getValor() == 0) return true;
        if(desAbajo.equals("v") && valor < celdaAbajo.getValor()) return false;
        if(desAbajo.equals("^") && valor > celdaAbajo.getValor()) return false;
        if(desDer.equals(">") && valor < celdaDerecha.getValor()) return false;
        if(desDer.equals("<") && valor > celdaDerecha.getValor()) return false;
        return true;
    }

    @Override
    public String toString() {
        return valor + " " + desDer;
    }
}