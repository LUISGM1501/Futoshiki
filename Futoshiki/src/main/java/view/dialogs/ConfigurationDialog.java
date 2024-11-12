package view.dialogs;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

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
        
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;


        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void initializeValues() {
        // Cargar valores desde la configuración actual
    }

    public Configuration getConfiguration() {
        Configuration config = new Configuration();
        // Obtener valores de los componentes y configurar el objeto
        return config;
    }
}
