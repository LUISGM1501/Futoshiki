package view.components;

import javax.swing.JButton;

/**
 * La clase NumberCell extiende JButton y representa una celda numerica en el juego Futoshiki.
 */
public class NumberCell extends JButton {
    private int valor;
    private String desDer;
    private String desAbajo;
    private boolean isConstant;

    /**
     * Constructor de la clase NumberCell.
     * Inicializa los valores por defecto y configura el botón.
     */
    public NumberCell() {
        super();
        this.valor = 0;
        this.desDer = " ";
        this.desAbajo = " ";
        this.isConstant = false;
        configureButton();
    }

    /**
     * Configura las propiedades del botón.
     * Desactiva el pintado del foco y activa el pintado del borde.
     */
    private void configureButton() {
        setFocusPainted(false);
        setBorderPainted(true);
    }
}
