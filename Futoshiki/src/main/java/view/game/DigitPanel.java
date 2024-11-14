package view.game;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;

public class DigitPanel extends JPanel {
    private JButton[] digitButtons;
    private JButton eraserButton;
    private int selectedDigit;
    private List<DigitSelectListener> digitListeners;

    public interface DigitSelectListener {
        void onDigitSelected(int digit);
    }

    public DigitPanel() {
        setLayout(new GridLayout(6, 1, 5, 5)); // 5 dígitos + borrador
        setPreferredSize(new Dimension(100, 300));
        digitListeners = new ArrayList<>();
        initializeComponents();
    }

    private void initializeComponents() {
        digitButtons = new JButton[5];
        
        // Crear botones de dígitos
        for (int i = 0; i < 5; i++) {
            digitButtons[i] = new JButton(String.valueOf(i + 1));
            final int digit = i + 1;
            digitButtons[i].addActionListener(e -> {
                setSelectedDigit(digit);
                notifyDigitSelected(digit);
            });
            add(digitButtons[i]);
        }

        // Agregar botón de borrador
        eraserButton = new JButton("⌫");
        add(eraserButton);
    }

    public void setSelectedDigit(int digit) {
        // Desmarcar el botón previamente seleccionado
        if (selectedDigit > 0 && selectedDigit <= 5) {
            digitButtons[selectedDigit - 1].setBackground(null);
        }
        
        // Marcar el nuevo botón seleccionado
        selectedDigit = digit;
        if (digit > 0 && digit <= 5) {
            digitButtons[digit - 1].setBackground(java.awt.Color.GREEN);
        }
    }

    public int getSelectedDigit() {
        return selectedDigit;
    }

    public void addDigitSelectListener(DigitSelectListener listener) {
        digitListeners.add(listener);
    }

    public void removeDigitSelectListener(DigitSelectListener listener) {
        digitListeners.remove(listener);
    }

    private void notifyDigitSelected(int digit) {
        for (DigitSelectListener listener : digitListeners) {
            listener.onDigitSelected(digit);
        }
    }
}