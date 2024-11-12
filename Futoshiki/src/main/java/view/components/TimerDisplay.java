package view.components;

import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class TimerDisplay extends JPanel {
    private JLabel hoursLabel;
    private JLabel minutesLabel;
    private JLabel secondsLabel;

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

    public void updateTime(int hours, int minutes, int seconds) {
        hoursLabel.setText(String.format("%02d", hours));
        minutesLabel.setText(String.format("%02d", minutes));
        secondsLabel.setText(String.format("%02d", seconds));
    }
}