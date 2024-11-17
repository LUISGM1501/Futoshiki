package controller.config;

import model.config.Configuration;
import view.dialogs.ConfigurationDialog;
import view.game.MainWindow;

public class ConfigurationController {
    private Configuration config;
    private MainWindow mainWindow;

    // Constructor
    public ConfigurationController(Configuration config, MainWindow mainWindow) {
        this.config = config;
        this.mainWindow = mainWindow;
    }




    public void setTimerType(String timerType)
    {
        config.setTimerType(timerType);
    }
    public String getCronometro()
    {
        return config.getTimerType();
    }
    public void showConfigDialog() {
        ConfigurationDialog dialog = new ConfigurationDialog(mainWindow);
        dialog.setVisible(true);
        
        if (dialog.isConfirmed()) {
            config = dialog.getConfiguration();
            mainWindow.setConfiguration(config);
        }

    }
}