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
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

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
    private boolean isConfirmed = false;

    /**
     * Constructor de la clase ConfigurationDialog.
     * 
     * @param owner El frame propietario del diálogo.
     */
    public ConfigurationDialog(Frame owner) {
        super(owner, "Configuración", true);
        initComponents();
        layoutComponents();
        initializeValues();
        setupListeners();
        pack();
        setLocationRelativeTo(owner);
    }

    /**
     * Inicializa los componentes del diálogo.
     */
    private void initComponents() {
        // Tamaño de cuadrícula
        String[] sizes = {"3 x 3", "4 x 4", "5 x 5"};
        gridSizeCombo = new JComboBox<>(sizes);
        gridSizeCombo.setSelectedItem("5 x 5"); // Default

        // Nivel del juego
        String[] difficulties = {"Fácil", "Intermedio","Aleatorio"};
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

    /**
     * Establece la configuración actual en el diálogo.
     * 
     * @param config La configuración a establecer.
     */
    public void setConfiguration(Configuration config) {
        // Establecer valores actuales de la configuración
        gridSizeCombo.setSelectedItem(String.valueOf(config.getGridSize()));
        difficultyCombo.setSelectedItem(config.getDifficulty());
        multiLevelCheck.setSelected(config.isMultiLevel());
        chronoButton.setSelected(config.getTimerType().equals("Cronómetro"));
        timerButton.setSelected(config.getTimerType().equals("Temporizador"));
        noTimerButton.setSelected(config.getTimerType().equals("No"));
        hoursSpinner.setValue(config.getTimerHours());
        minutesSpinner.setValue(config.getTimerMinutes());
        secondsSpinner.setValue(config.getTimerSeconds());
        rightPanelButton.setSelected(config.getDigitPanelPosition().equals("right"));
        leftPanelButton.setSelected(config.getDigitPanelPosition().equals("left"));
        playerNameField.setText(config.getPlayerName());
        isConfirmed = false;
    }

    /**
     * Organiza los componentes en el diálogo.
     */
    private void layoutComponents() {
        setLayout(new BorderLayout());
        
        JPanel mainPanel = new JPanel(new GridLayout(6, 1, 10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 20, 10, 20));

        // Panel para el tamaño de cuadrícula
        JPanel gridPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        gridPanel.add(new JLabel("Tamaño de cuadrícula:"));
        gridPanel.add(gridSizeCombo);
        mainPanel.add(gridPanel);

        // Panel para dificultad
        JPanel difficultyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        difficultyPanel.add(new JLabel("Nivel:"));
        difficultyPanel.add(difficultyCombo);
        mainPanel.add(difficultyPanel);

        // Panel para multinivel
        JPanel multiLevelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        multiLevelPanel.add(multiLevelCheck);
        mainPanel.add(multiLevelPanel);

        // Panel para el timer
        JPanel timerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        timerPanel.add(new JLabel("Timer:"));
        timerPanel.add(chronoButton);
        timerPanel.add(noTimerButton);
        timerPanel.add(timerButton);
        mainPanel.add(timerPanel);

        // Panel para posición de dígitos
        JPanel positionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        positionPanel.add(new JLabel("Posición del panel:"));
        positionPanel.add(rightPanelButton);
        positionPanel.add(leftPanelButton);
        mainPanel.add(positionPanel);

        // Panel de spinners
        JPanel spinnerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        spinnerPanel.add(new JLabel("H:"));
        spinnerPanel.add(hoursSpinner);
        spinnerPanel.add(new JLabel("M:"));
        spinnerPanel.add(minutesSpinner);
        spinnerPanel.add(new JLabel("S:"));
        spinnerPanel.add(secondsSpinner);
        mainPanel.add(spinnerPanel);

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Inicializa los valores del diálogo.
     */
    private void initializeValues() {
        // Cargar valores desde la configuración actual
    }

    /**
     * Configura los listeners para los botones.
     */
    private void setupListeners() {
        okButton.addActionListener(e -> exit());
    }

    /**
     * Cierra el diálogo.
     */
    private void exit() {
        System.out.println(getTimerType());
        System.out.println(secondsSpinner.getValue());
        dispose();
    }

    /**
     * Obtiene el tamaño de la cuadrícula seleccionada.
     * 
     * @return El tamaño de la cuadrícula.
     */
    private int getGridSize() {
        char gridSize = gridSizeCombo.getSelectedItem().toString().charAt(0);
        return Character.getNumericValue(gridSize);
    }

    /**
     * Obtiene el tipo de temporizador seleccionado.
     * 
     * @return El tipo de temporizador.
     */
    private String getTimerType() {
        if (timerButton.isSelected()) {
            return timerButton.getText();
        } else if (chronoButton.isSelected()) {
            return chronoButton.getText();
        }
        return noTimerButton.getText();
    }

    /**
     * Obtiene la configuración actual del diálogo.
     * 
     * @return La configuración actual.
     */
    public Configuration getConfiguration() {
        Configuration config = new Configuration();
        config.setTimerType(getTimerType());
        config.setTimerSeconds((Integer) secondsSpinner.getValue());
        config.setTimerMinutes((Integer) minutesSpinner.getValue());
        config.setTimerHours((Integer) hoursSpinner.getValue());
        config.setDifficulty(difficultyCombo.getSelectedItem().toString());
        config.setGridSize(getGridSize());
        // Establecer la posición del panel
        config.setDigitPanelPosition(leftPanelButton.isSelected() ? "left" : "right");
        return config;
    }

    /**
     * Verifica si la configuración fue confirmada.
     * 
     * @return true si fue confirmada, false en caso contrario.
     */
    public boolean isConfirmed() {
        return isConfirmed;
    }

    /**
     * Establece si la configuración fue confirmada.
     * 
     * @param confirmed true si fue confirmada, false en caso contrario.
     */
    public void setConfirmed(boolean confirmed) {
        this.isConfirmed = confirmed;
    }
}
