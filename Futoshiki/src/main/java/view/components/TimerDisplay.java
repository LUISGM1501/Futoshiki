package view.components;

import java.awt.FlowLayout;
import javax.swing.*;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class TimerDisplay extends JPanel {
    private JLabel hoursLabel;
    private JLabel minutesLabel;
    private JLabel secondsLabel;
    private int secondsPassed = 0, minutesPassed =0, hoursPassed =0;
    private Timer timer;

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


    //Retorna los valores actuales del timer
    public String toString()
    {
        return hoursPassed + ":" + minutesPassed + ":" + secondsPassed;
    }

    //Uso de esto?
    public void updateTime(int hours, int minutes, int seconds) {
        hoursLabel.setText(String.format("%02d", hours));
        minutesLabel.setText(String.format("%02d", minutes));
        secondsLabel.setText(String.format("%02d", seconds));
    }

    //Comienza a actualizar el tiempo segundo por segundo
    public void actualizarTiempo()
    {
        secondsPassed++;
        if(secondsPassed >= 60)
        {
            secondsPassed = 0;
            minutesPassed += 1;
        }
        if(minutesPassed >= 60)
        {
            hoursPassed +=1;
            minutesPassed = 0;
        }

    }
}