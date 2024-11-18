package view.dialogs;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;

public class GameSetupDialog extends JDialog {
    private String selectedDifficulty;
    private int selectedSize;
    private boolean confirmed;
    private ButtonGroup timerGroup;
    private JRadioButton chronoButton;
    private JRadioButton noTimerButton;
    private JRadioButton timerButton;
    private JSpinner hoursSpinner;
    private JSpinner minutesSpinner;
    private JSpinner secondsSpinner;

    public GameSetupDialog(Frame owner) {
        super(owner, "Configuración de Partida", true);
        setLayout(new BorderLayout(10, 10));
        setBounds(0, 0, 300, 400);
        setLocationRelativeTo(owner);

        // Panel principal
        JPanel mainPanel = new JPanel(new GridLayout(5, 1, 10, 10));
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


        //Botones de tipos de Timers
        JPanel typeTimerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        timerGroup = new ButtonGroup();
        chronoButton = new JRadioButton("Cronómetro");
        noTimerButton = new JRadioButton("No");
        timerButton = new JRadioButton("Temporizador");
        timerGroup.add(chronoButton);
        timerGroup.add(noTimerButton);
        timerGroup.add(timerButton);
        chronoButton.setSelected(true);
        typeTimerPanel.add(chronoButton);
        typeTimerPanel.add(noTimerButton);
        typeTimerPanel.add(timerButton);
        mainPanel.add(typeTimerPanel);


        //Spinners de Tiempo
        JPanel spinnerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        hoursSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 5, 1));
        minutesSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 59, 1));
        secondsSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 59, 1));

        spinnerPanel.add(hoursSpinner);
        spinnerPanel.add(minutesSpinner);
        spinnerPanel.add(secondsSpinner);
        mainPanel.add(spinnerPanel);


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

    public int getSeconds()
    {
        return (Integer) secondsSpinner.getValue();
    }

    public int getMinutes()
    {
        return (Integer) minutesSpinner.getValue();
    }

    public int getHours()
    {
        return (Integer) hoursSpinner.getValue();
    }
    public boolean isConfirmed() { return confirmed; }
    public String getSelectedDifficulty() { return selectedDifficulty; }
    public int getSelectedSize() { return selectedSize; }
    public String getTimerType()
    {
        if(timerButton.isSelected()){return timerButton.getText();}
        if(chronoButton.isSelected()){return chronoButton.getText();}
        return "";
    }
}