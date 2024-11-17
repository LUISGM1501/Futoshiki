package view.dialogs;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;

public class GameSetupDialog extends JDialog {
    private String selectedDifficulty;
    private int selectedSize;
    private boolean confirmed;

    public GameSetupDialog(Frame owner) {
        super(owner, "Configuración de Partida", true);
        setLayout(new BorderLayout(10, 10));
        setBounds(0, 0, 300, 200);
        setLocationRelativeTo(owner);

        // Panel principal
        JPanel mainPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 20, 10, 20));

        // Selector de dificultad
        JPanel difficultyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        difficultyPanel.add(new JLabel("Dificultad:"));
        JComboBox<String> difficultyCombo = new JComboBox<>(
            new String[]{"Facil", "Intermedio", "Dificil"}
        );
        difficultyPanel.add(difficultyCombo);
        mainPanel.add(difficultyPanel);

        // Selector de tamaño
        JPanel sizePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        sizePanel.add(new JLabel("Tamaño:"));
        JComboBox<String> sizeCombo = new JComboBox<>(
            new String[]{"3x3", "4x4", "5x5"}
        );
        sizePanel.add(sizeCombo);
        mainPanel.add(sizePanel);

        // Botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton okButton = new JButton("Comenzar");
        JButton cancelButton = new JButton("Cancelar");
        
        okButton.addActionListener(e -> {
            selectedDifficulty = (String)difficultyCombo.getSelectedItem();
            selectedSize = Integer.parseInt(
                ((String)sizeCombo.getSelectedItem()).split("x")[0]
            );
            confirmed = true;
            dispose();
        });
        
        cancelButton.addActionListener(e -> dispose());
        
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        mainPanel.add(buttonPanel);

        add(mainPanel, BorderLayout.CENTER);
    }

    public boolean isConfirmed() { return confirmed; }
    public String getSelectedDifficulty() { return selectedDifficulty; }
    public int getSelectedSize() { return selectedSize; }
}