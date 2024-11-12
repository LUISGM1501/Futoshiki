package view.components;

import javax.swing.JButton;

public class NumberCell extends JButton {
    private int valor;
    private String desDer;
    private String desAbajo;
    private boolean isConstant;

    public NumberCell() {
        super();
        this.valor = 0;
        this.desDer = " ";
        this.desAbajo = " ";
        this.isConstant = false;
        configureButton();
    }

    private void configureButton() {
        setFocusPainted(false);
        setBorderPainted(true);
    }
}
