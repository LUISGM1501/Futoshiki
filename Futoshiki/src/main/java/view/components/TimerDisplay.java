package view.components;

import java.awt.FlowLayout;
import javax.swing.*;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * La clase TimerDisplay representa un componente visual para mostrar un temporizador.
 */
public class TimerDisplay extends JPanel {
    private JLabel hoursLabel;
    private JLabel minutesLabel;
    private JLabel secondsLabel;
    private int secondsPassed = 0, minutesPassed = 0, hoursPassed = 0;
    private Timer timer;

    /**
     * Constructor de la clase TimerDisplay.
     * Inicializa las etiquetas de horas, minutos y segundos y las añade al panel.
     */
    public TimerDisplay() {
        setLayout(new FlowLayout());

        hoursLabel = new JLabel("00");
        minutesLabel = new JLabel("00");
        secondsLabel = new JLabel("00");
        
        add(hoursLabel);
        add(new JLabel(":"));
        add(minutesLabel);
        add(new JLabel(":"));
        add(secondsLabel);
    }

    // Retorna los valores actuales del timer
    /**
     * Devuelve una representación en cadena del tiempo transcurrido en formato HH:MM:SS.
     * 
     * @return Una cadena que representa el tiempo transcurrido.
     */
    public String toString() {
        return hoursPassed + ":" + minutesPassed + ":" + secondsPassed;
    }

    // Uso de esto?
    /**
     * Actualiza las etiquetas de tiempo con los valores proporcionados.
     * 
     * @param hours Las horas a mostrar.
     * @param minutes Los minutos a mostrar.
     * @param seconds Los segundos a mostrar.
     */
    public void updateTime(int hours, int minutes, int seconds) {
        hoursLabel.setText(String.format("%02d", hours));
        minutesLabel.setText(String.format("%02d", minutes));
        secondsLabel.setText(String.format("%02d", seconds));
    }

    // Comienza a actualizar el tiempo segundo por segundo
    /**
     * Incrementa el tiempo transcurrido en un segundo y actualiza las horas, minutos y segundos en consecuencia.
     */
    public void actualizarTiempo() {
        secondsPassed++;
        if (secondsPassed >= 60) {
            secondsPassed = 0;
            minutesPassed += 1;
        }
        if (minutesPassed >= 60) {
            hoursPassed += 1;
            minutesPassed = 0;
        }
    }
}