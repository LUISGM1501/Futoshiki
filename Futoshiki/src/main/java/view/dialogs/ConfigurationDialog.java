package view.dialogs;

import java.awt.*;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import model.config.Configuration;

public class ConfigurationDialog extends JDialog {
    private JComboBox<String> gridSizeCombo;
    private JComboBox<String> difficultyCombo;
    private JCheckBox multiLevelCheck;
    private ButtonGroup timerGroup;
    private JRadioButton chronoButton;
    private JRadioButton noTimerButton;
    private JRadioButton timerButton;
    private JSpinner hoursSpinner;
    private JSpinner minutesSpinner;
    private JSpinner secondsSpinner;
    private ButtonGroup digitPanelGroup;
    private JRadioButton rightPanelButton;
    private JRadioButton leftPanelButton;
    private JTextField playerNameField;
    private JButton okButton;
    private JButton cancelButton;

    public ConfigurationDialog(Frame owner) {
        super(owner, "Configuración", true);
        initComponents();
        layoutComponents();
        initializeValues();
        setupListeners();
        pack();
        setLocationRelativeTo(owner);
    }

    private void initComponents() {
        // Tamaño de cuadrícula
        String[] sizes = {"3 x 3", "4 x 4", "5 x 5"};
        gridSizeCombo = new JComboBox<>(sizes);
        gridSizeCombo.setSelectedItem("5 x 5"); // Default

        // Nivel del juego
        String[] difficulties = {"Fácil", "Intermedio", "Difícil"};
        difficultyCombo = new JComboBox<>(difficulties);

        // Multinivel
        multiLevelCheck = new JCheckBox("Multinivel");

        // Uso del reloj
        timerGroup = new ButtonGroup();
        chronoButton = new JRadioButton("Cronómetro");
        noTimerButton = new JRadioButton("No");
        timerButton = new JRadioButton("Temporizador");
        timerGroup.add(chronoButton);
        timerGroup.add(noTimerButton);
        timerGroup.add(timerButton);
        chronoButton.setSelected(true);

        // Spinners para el tiempo
        hoursSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 5, 1));
        minutesSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 59, 1));
        secondsSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 59, 1));

        // Posición del panel de dígitos
        digitPanelGroup = new ButtonGroup();
        rightPanelButton = new JRadioButton("Derecha");
        leftPanelButton = new JRadioButton("Izquierda");
        digitPanelGroup.add(rightPanelButton);
        digitPanelGroup.add(leftPanelButton);
        rightPanelButton.setSelected(true);

        // Nombre del jugador
        playerNameField = new JTextField(20);

        // Botones
        okButton = new JButton("Aceptar");
        cancelButton = new JButton("Cancelar");
    }

    private void layoutComponents() {
        setLayout(new BorderLayout());
        
        JPanel mainPanel = new JPanel(new GridLayout(4,0));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.NORTH;



        //Radio Buttons
        JPanel rButtonPanel = new JPanel(new GridLayout(6,0));
        rButtonPanel.add(chronoButton);
        rButtonPanel.add(timerButton);
        rButtonPanel.add(noTimerButton);

        //Spinners
        JPanel timerSpinners = new JPanel(new GridLayout(0, 4));
        timerSpinners.add(hoursSpinner);
        timerSpinners.add(minutesSpinner);
        timerSpinners.add(secondsSpinner);
        rButtonPanel.add(timerSpinners,BorderLayout.EAST);


        mainPanel.add(difficultyCombo);
        mainPanel.add(gridSizeCombo);
        mainPanel.add(multiLevelCheck);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        add(rButtonPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        //add(rButtonPanel, BorderLayout.SOUTH);
    }

    private void initializeValues() {
        // Cargar valores desde la configuración actual
    }


    private void setupListeners()
    {
        okButton.addActionListener(e -> exit());
    }


    private void exit()
    {
        System.out.println(getTimerType());
        System.out.println(secondsSpinner.getValue());
        dispose();
    }

    private int getGridSize()
    {
        char gridSize = gridSizeCombo.getSelectedItem().toString().charAt(0);

        return Character.getNumericValue(gridSize);
    }

    private String getTimerType()
    {
        if(timerButton.isSelected())
        {
            return timerButton.getText();
        }else if(chronoButton.isSelected())
        {
            return chronoButton.getText();
        }

        return noTimerButton.getText();
    }

    public Configuration getConfiguration() {
        Configuration config = new Configuration();
        config.setTimerType(getTimerType());
        //Podriamos hacer una revision de que si es un timer o un cronometro
        //Si es cronometro los comienza en 0, si es en timer lo comienza con lo que el mae escogio
        config.setTimerType(getTimerType());
        config.setTimerSeconds((Integer) secondsSpinner.getValue());
        config.setTimerMinutes((Integer) minutesSpinner.getValue());
        config.setTimerHours((Integer) hoursSpinner.getValue());
        config.setDifficulty(difficultyCombo.getSelectedItem().toString());
        config.setGridSize(getGridSize());
        return config;
    }
}
