package persistence;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import model.config.Configuration;
import util.constants.FileConstants;
import util.validators.InputValidator;

public class ConfigurationManager {
    
    // Guarda la configuración en el archivo
    // @return true si se guardó correctamente
    public static boolean saveConfiguration(Configuration config) {
        // Validar la configuración antes de guardar
        String validationError = InputValidator.validateConfiguration(config);
        if (validationError != null) {
            return false;
        }
        
        try (ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream(FileConstants.CONFIG_FILE))) {
            
            out.writeObject(config);
            return true;
            
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Carga la configuración del archivo
    // @return Configuration con valores por defecto si no existe archivo
    public static Configuration loadConfiguration() {
        try (ObjectInputStream in = new ObjectInputStream(
                new FileInputStream(FileConstants.CONFIG_FILE))) {
            
            return (Configuration) in.readObject();
            
        } catch (IOException | ClassNotFoundException e) {
            // Si hay error o no existe archivo, retornar configuración por defecto
            return new Configuration();
        }
    }
    
    // Verifica si existe una configuración guardada    
    public static boolean configurationExists() {
        File configFile = new File(FileConstants.CONFIG_FILE);
        return configFile.exists() && configFile.length() > 0;
    }
}