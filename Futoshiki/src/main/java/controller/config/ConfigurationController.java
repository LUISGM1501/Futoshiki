package controller.config;

import model.config.Configuration;
import persistence.ConfigurationManager;
import view.dialogs.ConfigurationDialog;
import view.game.MainWindow;

/**
 * Controlador para gestionar la configuración de la aplicación.
 */
public class ConfigurationController {
    private Configuration config;
    private MainWindow mainWindow;

    /**
     * Constructor de ConfigurationController.
     * 
     * @param config La configuración por defecto a usar si no se encuentra una guardada.
     * @param mainWindow La ventana principal de la aplicación.
     */
    public ConfigurationController(Configuration config, MainWindow mainWindow) {
        System.out.println("ConfigurationController: Constructor iniciado");
        this.mainWindow = mainWindow;
        
        // Cargar configuración guardada o usar la proporcionada como default
        Configuration loadedConfig = ConfigurationManager.loadConfiguration();
        if (loadedConfig != null) {
            this.config = loadedConfig;
        } else {
            this.config = config;
            ConfigurationManager.saveConfiguration(this.config);
        }
        
        // Actualizar la ventana con la configuración
        mainWindow.setConfiguration(this.config);
    }

    /**
     * Establece el tipo de temporizador.
     * 
     * @param timerType El tipo de temporizador a establecer.
     */
    public void setTimerType(String timerType) {
        config.setTimerType(timerType);
    }

    /**
     * Obtiene el tipo de cronómetro configurado.
     * 
     * @return El tipo de cronómetro.
     */
    public String getCronometro() {
        return config.getTimerType();
    }

    /**
     * Muestra el diálogo de configuración y guarda los cambios si se confirman.
     */
    public void showConfigDialog() {
        System.out.println("ConfigurationController: Mostrando diálogo de configuración");

        // Crear y mostrar el diálogo de configuración
        ConfigurationDialog dialog = new ConfigurationDialog(mainWindow);
        dialog.setConfiguration(config); // Establecer la configuración actual
        dialog.setVisible(true);
        
        if (dialog.isConfirmed()) {
            System.out.println("ConfigurationController: Configuración confirmada, guardando...");
            config = dialog.getConfiguration();
            mainWindow.setConfiguration(config);
            ConfigurationManager.saveConfiguration(config);
            System.out.println("ConfigurationController: Configuración guardada");
        }
    }

    /**
     * Actualiza la configuración actual con una nueva configuración.
     * 
     * @param newConfig La nueva configuración a establecer.
     */
    public void updateConfiguration(Configuration newConfig) {
        System.out.println("ConfigurationController: Actualizando configuración");
        this.config = newConfig;
        mainWindow.setConfiguration(config);
        ConfigurationManager.saveConfiguration(config);
    }

    /**
     * Método para obtener la configuración actual.
     * 
     * @return La configuración actual.
     */
    public Configuration getConfiguration() {
        return config;
    }
}