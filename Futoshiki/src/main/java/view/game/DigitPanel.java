package view.game;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * Clase DigitPanel que representa un panel de botones numéricos y un botón de borrador.
 */
public class DigitPanel extends JPanel {
    private JButton[] digitButtons;
    private JButton eraserButton;
    private int selectedDigit;
    private List<DigitSelectListener> digitListeners;
    private int maxDigits;

    /**
     * Interfaz para escuchar la selección de dígitos.
     */
    public interface DigitSelectListener {
        /**
         * Método llamado cuando se selecciona un dígito.
         * 
         * @param digit El dígito seleccionado.
         */
        void onDigitSelected(int digit);
    }

    /**
     * Constructor de DigitPanel que inicializa el panel con un layout dinámico y componentes.
     */
    public DigitPanel() {
        setLayout(new GridLayout(0, 1, 5, 5)); // Layout dinámico
        digitListeners = new ArrayList<>();
        maxDigits = 5; // Valor por defecto
        initializeComponents();
    }

    /**
     * Establece el número máximo de dígitos y actualiza el panel.
     * 
     * @param size El nuevo tamaño máximo de dígitos.
     */
    public void setMaxDigits(int size) {
        System.out.println("Actualizando panel de dígitos a tamaño: " + size); // Debug
        if (this.maxDigits != size) {
            this.maxDigits = size;
            removeAll(); // Eliminar todos los botones actuales
            
            // Crear nuevo layout con el número correcto de filas
            setLayout(new GridLayout(size + 1, 1, 5, 5)); // +1 para el botón de borrador
            
            // Reinicializar los botones con el nuevo tamaño
            digitButtons = new JButton[size];
            
            // Crear los botones con los números correctos
            for (int i = 0; i < size; i++) {
                final int digit = i + 1;
                digitButtons[i] = new JButton(String.valueOf(digit));
                digitButtons[i].setPreferredSize(new Dimension(60, 40));
                digitButtons[i].setFont(new Font("Arial", Font.PLAIN, 14));
                digitButtons[i].addActionListener(e -> {
                    setSelectedDigit(digit);
                    notifyDigitSelected(digit);
                });
                add(digitButtons[i]);
            }
            
            // Agregar el botón de borrador
            eraserButton = new JButton("⌫");
            eraserButton.setPreferredSize(new Dimension(60, 40));
            add(eraserButton);

            // Forzar actualización del layout
            revalidate();
            repaint();
            
            System.out.println("Panel de dígitos actualizado con " + size + " botones"); // Debug
        }
    }

    /**
     * Inicializa los componentes del panel.
     */
    private void initializeComponents() {
        digitButtons = new JButton[maxDigits];
        
        // Crear botones de dígitos dinámicamente según el tamaño
        for (int i = 0; i < maxDigits; i++) {
            digitButtons[i] = new JButton(String.valueOf(i + 1));
            final int digit = i + 1;
            
            // Configurar el estilo del botón
            digitButtons[i].setFont(new Font("Arial", Font.PLAIN, 14));
            digitButtons[i].setFocusPainted(false);
            
            digitButtons[i].addActionListener(e -> {
                setSelectedDigit(digit);
                notifyDigitSelected(digit);
            });
            add(digitButtons[i]);
        }

        // Agregar botón de borrador
        eraserButton = new JButton("⌫");
        eraserButton.setFont(new Font("Arial", Font.PLAIN, 14));
        eraserButton.setFocusPainted(false);
        add(eraserButton);
    }

    /**
     * Establece el dígito seleccionado y actualiza la interfaz.
     * 
     * @param digit El dígito a seleccionar.
     */
    public void setSelectedDigit(int digit) {
        // Desmarcar el botón previamente seleccionado
        if (selectedDigit > 0 && selectedDigit <= maxDigits) {
            digitButtons[selectedDigit - 1].setBackground(null);
        }
        
        // Marcar el nuevo botón seleccionado
        selectedDigit = digit;
        if (digit > 0 && digit <= maxDigits) {
            digitButtons[digit - 1].setBackground(java.awt.Color.GREEN);
        }
    }

    /**
     * Obtiene el dígito actualmente seleccionado.
     * 
     * @return El dígito seleccionado.
     */
    public int getSelectedDigit() {
        return selectedDigit;
    }

    /**
     * Añade un listener para la selección de dígitos.
     * 
     * @param listener El listener a añadir.
     */
    public void addDigitSelectListener(DigitSelectListener listener) {
        digitListeners.add(listener);
    }

    /**
     * Elimina un listener de la selección de dígitos.
     * 
     * @param listener El listener a eliminar.
     */
    public void removeDigitSelectListener(DigitSelectListener listener) {
        digitListeners.remove(listener);
    }

    /**
     * Notifica a los listeners que un dígito ha sido seleccionado.
     * 
     * @param digit El dígito seleccionado.
     */
    private void notifyDigitSelected(int digit) {
        for (DigitSelectListener listener : digitListeners) {
            listener.onDigitSelected(digit);
        }
    }
}