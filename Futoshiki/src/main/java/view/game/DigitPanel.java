package view.game;

import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase DigitPanel que representa un panel de botones numéricos y un botón de borrador.
 */
public class DigitPanel extends JPanel {
    private JButton[] digitButtons;
    private JButton eraserButton;
    private int selectedDigit;
    private List<DigitSelectListener> digitListeners;
    private int maxDigits;
    private volatile boolean eraserSelected;  // Hacer el estado volátil para asegurar visibilidad

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
        setLayout(new BorderLayout(5, 5));
        digitListeners = new ArrayList<>();
        maxDigits = 5; // Valor por defecto
        eraserSelected = false;
        initializeComponents();
    }

    /**
     * Inicializa los componentes del panel.
     */
    private void initializeComponents() {
        // Panel para los dígitos
        JPanel digitsPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        digitButtons = new JButton[maxDigits];
        
        for (int i = 0; i < maxDigits; i++) {
            digitButtons[i] = createDigitButton(i + 1);
            digitsPanel.add(digitButtons[i]);
        }

        // Crear el botón borrador con estilo
        eraserButton = new JButton("Borrador ⌫");
        eraserButton.setFont(new Font("Arial", Font.BOLD, 14));
        eraserButton.setFocusPainted(false);
        eraserButton.setPreferredSize(new Dimension(60, 40));
        eraserButton.setBorder(BorderFactory.createLineBorder(Color.RED));

        eraserButton.addActionListener(e -> {
            System.out.println("DigitPanel - Botón borrador presionado");
            toggleEraser();
            System.out.println("DigitPanel - Estado después de toggle:");
            System.out.println("  eraserSelected: " + eraserSelected);
            System.out.println("  selectedDigit: " + selectedDigit);
            notifyDigitSelected(-1);
        });

        // Organizar los componentes
        add(digitsPanel, BorderLayout.CENTER);
        add(eraserButton, BorderLayout.NORTH);
    }

    /**
     * Crea un botón de dígito con el estilo y comportamiento adecuado.
     * 
     * @param digit El dígito para el botón.
     * @return El botón de dígito creado.
     */
    private JButton createDigitButton(int digit) {
        JButton button = new JButton(String.valueOf(digit));
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(60, 40));
        
        button.addActionListener(e -> {
            System.out.println("Dígito " + digit + " seleccionado. Estado del borrador: " + eraserSelected);
            if (!eraserSelected) {
                setSelectedDigit(digit);
                notifyDigitSelected(digit);
            }
        });
        
        return button;
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
            removeAll();
            
            // Panel para los dígitos
            JPanel digitsPanel = new JPanel(new GridLayout(size, 1, 5, 5));
            digitButtons = new JButton[size];
            
            for (int i = 0; i < size; i++) {
                digitButtons[i] = createDigitButton(i + 1);
                digitsPanel.add(digitButtons[i]);
            }
            
            // Recrear el borrador
            eraserButton = new JButton("Borrador ⌫");
            eraserButton.setFont(new Font("Arial", Font.BOLD, 14));
            eraserButton.setFocusPainted(false);
            eraserButton.setPreferredSize(new Dimension(60, 40));
            eraserButton.setBorder(BorderFactory.createLineBorder(Color.RED));

            eraserButton.addActionListener(e -> {
                System.out.println("DigitPanel - Botón borrador presionado");
                toggleEraser();
                System.out.println("DigitPanel - Estado después de toggle:");
                System.out.println("  eraserSelected: " + eraserSelected);
                System.out.println("  selectedDigit: " + selectedDigit);
                notifyDigitSelected(-1);
            });

            // También necesitamos actualizar los botones de dígitos para que desactiven el borrador
            for (int i = 0; i < maxDigits; i++) {
                final int digit = i + 1;
                digitButtons[i].addActionListener(e -> {
                    // Desactivar borrador si estaba activo
                    if (eraserSelected) {
                        eraserSelected = false;
                        eraserButton.setBackground(null);
                    }
                    setSelectedDigit(digit);
                    notifyDigitSelected(digit);
                });
            }

            // Organizar los componentes
            add(digitsPanel, BorderLayout.CENTER);
            add(eraserButton, BorderLayout.NORTH);

            revalidate();
            repaint();
            
            System.out.println("Panel de dígitos actualizado con " + size + " botones"); // Debug
        }
    }

    /**
     * Establece el dígito seleccionado y actualiza la interfaz.
     * 
     * @param digit El dígito a seleccionar.
     */
    public void setSelectedDigit(int digit) {
        // Solo cambiar dígito si el borrador no está activo
        if (!eraserSelected) {
            if (selectedDigit > 0 && selectedDigit <= maxDigits) {
                digitButtons[selectedDigit - 1].setBackground(null);
            }
            selectedDigit = digit;
            if (digit > 0 && digit <= maxDigits) {
                digitButtons[digit - 1].setBackground(Color.GREEN);
            }
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

    /**
     * Verifica si el borrador está seleccionado.
     * 
     * @return true si el borrador está seleccionado, de lo contrario false.
     */
    public boolean isEraserSelected() {
        return eraserSelected;
    }

    /**
     * Cambia el estado del borrador y actualiza la interfaz.
     */
    private void toggleEraser() {
        eraserSelected = !eraserSelected;
        if (eraserSelected) {
            // Desmarcar dígitos seleccionados
            if (selectedDigit > 0 && selectedDigit <= maxDigits) {
                digitButtons[selectedDigit - 1].setBackground(null);
            }
            selectedDigit = -1;
            eraserButton.setBackground(Color.RED);
        } else {
            eraserButton.setBackground(null);
        }
        System.out.println("Estado del borrador cambiado a: " + eraserSelected);
    }
}