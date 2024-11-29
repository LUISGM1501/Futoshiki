package view.dialogs;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;
import model.config.Configuration;

public class GameSetupDialog extends JDialog {
    private String selectedDifficulty;
    private int selectedSize;
    private boolean confirmed;
    private JCheckBox multiNivel;
    private ButtonGroup timerGroup;
    private JRadioButton chronoButton;
    private JRadioButton noTimerButton;
    private JRadioButton timerButton;
    private JSpinner hoursSpinner;
    private JSpinner minutesSpinner;
    private JSpinner secondsSpinner;
    private ButtonGroup positionGroup;
    private JRadioButton rightPositionButton;
    private JRadioButton leftPositionButton;
    private String selectedPosition;
    private JComboBox<String> sizeCombo; // Añadir como campo de clase
    private JComboBox<String> difficultyCombo;

    /**
     * Constructor de la clase GameSetupDialog.
     * 
     * @param owner El frame propietario del diálogo.
     */
    public GameSetupDialog(Frame owner) {
        super(owner, "Configuración de Partida", true);
        setLayout(new BorderLayout(10, 10));
        setBounds(0, 0, 300, 450);
        setLocationRelativeTo(owner);

        // Panel principal
        JPanel mainPanel = new JPanel(new GridLayout(6, 1, 10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 20, 10, 20));

        // Selector de dificultad
        JPanel difficultyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        difficultyPanel.add(new JLabel("Dificultad:"));
        JComboBox<String> difficultyCombo = new JComboBox<>(new String[]{"Facil", "Intermedio", "Dificil"});

        difficultyPanel.add(difficultyCombo);
        mainPanel.add(difficultyPanel);

        // Selector de tamaño actualizado para soportar hasta 10x10
        JPanel sizePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        sizePanel.add(new JLabel("Tamaño:"));
        String[] sizes = new String[8]; // Del 3x3 al 10x10
        for (int i = 0; i < 8; i++) {
            int size = i + 3;
            sizes[i] = size + "x" + size;
        }
        sizeCombo = new JComboBox<>(sizes);
        sizeCombo.setSelectedIndex(2); // Seleccionar 5x5 por defecto
        sizePanel.add(sizeCombo);
        mainPanel.add(sizePanel);

        // Botones de tipos de Timers
        JPanel typeTimerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        timerGroup = new ButtonGroup();
        chronoButton = new JRadioButton("Cronómetro");
        noTimerButton = new JRadioButton("No");
        timerButton = new JRadioButton("Temporizador");
        multiNivel = new JCheckBox("Multinvel");

        timerGroup.add(chronoButton);
        timerGroup.add(noTimerButton);
        timerGroup.add(timerButton);
        chronoButton.setSelected(true);
        typeTimerPanel.add(multiNivel);
        typeTimerPanel.add(chronoButton);
        typeTimerPanel.add(noTimerButton);
        typeTimerPanel.add(timerButton);
        mainPanel.add(typeTimerPanel);

        // Spinners de Tiempo
        JPanel spinnerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        hoursSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 5, 1));
        minutesSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 59, 1));
        secondsSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 59, 1));

        spinnerPanel.add(hoursSpinner);
        spinnerPanel.add(minutesSpinner);
        spinnerPanel.add(secondsSpinner);
        mainPanel.add(spinnerPanel);

        // Panel de posición
        JPanel positionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        positionPanel.add(new JLabel("Posición del panel:"));
        positionGroup = new ButtonGroup();
        rightPositionButton = new JRadioButton("Derecha");
        leftPositionButton = new JRadioButton("Izquierda");
        rightPositionButton.setSelected(true); // Por defecto a la derecha

        positionGroup.add(rightPositionButton);
        positionGroup.add(leftPositionButton);

        positionPanel.add(rightPositionButton);
        positionPanel.add(leftPositionButton);
        mainPanel.add(positionPanel);

        // Botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton okButton = new JButton("Comenzar");
        JButton cancelButton = new JButton("Cancelar");

        okButton.addActionListener(e -> {
            selectedDifficulty = (String) difficultyCombo.getSelectedItem();
            // Extraer el número del string "NxN"
            String sizeStr = ((String) sizeCombo.getSelectedItem()).split("x")[0];
            selectedSize = Integer.parseInt(sizeStr);
            selectedPosition = leftPositionButton.isSelected() ? "left" : "right";
            confirmed = true;
            dispose();
        });

        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        mainPanel.add(buttonPanel);

        add(mainPanel, BorderLayout.CENTER);
    }

    /**
     * Obtiene los segundos seleccionados en el spinner.
     * 
     * @return Los segundos seleccionados.
     */
    public int getSeconds() {
        return (Integer) secondsSpinner.getValue();
    }

    /**
     * Obtiene los minutos seleccionados en el spinner.
     * 
     * @return Los minutos seleccionados.
     */
    public int getMinutes() {
        return (Integer) minutesSpinner.getValue();
    }

    /**
     * Obtiene las horas seleccionadas en el spinner.
     * 
     * @return Las horas seleccionadas.
     */
    public int getHours() {
        return (Integer) hoursSpinner.getValue();
    }

    /**
     * Verifica si la configuración fue confirmada.
     * 
     * @return true si fue confirmada, false en caso contrario.
     */
    public boolean isConfirmed() {
        return confirmed;
    }

    /**
     * Obtiene la dificultad seleccionada.
     * 
     * @return La dificultad seleccionada.
     */
    public String getSelectedDifficulty() {
        return selectedDifficulty;
    }

    /**
     * Obtiene el tamaño seleccionado.
     * 
     * @return El tamaño seleccionado.
     */
    public int getSelectedSize() {
        return selectedSize;
    }

    /**
     * Obtiene el tipo de temporizador seleccionado.
     * 
     * @return El tipo de temporizador seleccionado.
     */
    public String getTimerType() {
        if (timerButton.isSelected()) {
            return timerButton.getText();
        }
        if (chronoButton.isSelected()) {
            return chronoButton.getText();
        }
        return "";
    }

    /**
     * Verifica si la opción multinivel está seleccionada.
     * 
     * @return true si está seleccionada, false en caso contrario.
     */
    public boolean isMultiNivel() {
        return multiNivel.isSelected();
    }

    /**
     * Obtiene la posición seleccionada del panel.
     * 
     * @return La posición seleccionada del panel.
     */
    public String getSelectedPosition() {
        return selectedPosition;
    }

    /**
     * Establece la configuración actual en el diálogo.
     * 
     * @param config La configuración a establecer.
     */
    public void setCurrentConfiguration(Configuration config) {
        // Establecer valores actuales de la configuración
        sizeCombo.setSelectedItem(String.valueOf(config.getGridSize()));
        difficultyCombo.setSelectedItem(config.getDifficulty());
        multiNivel.setSelected(config.isMultiLevel());
        chronoButton.setSelected(config.getTimerType().equals("Cronómetro"));
        timerButton.setSelected(config.getTimerType().equals("Temporizador"));
        noTimerButton.setSelected(config.getTimerType().equals("No"));
        hoursSpinner.setValue(config.getTimerHours());
        minutesSpinner.setValue(config.getTimerMinutes());
        secondsSpinner.setValue(config.getTimerSeconds());
        rightPositionButton.setSelected(config.getDigitPanelPosition().equals("right"));
        leftPositionButton.setSelected(config.getDigitPanelPosition().equals("left"));
        confirmed = false;
    }
}
