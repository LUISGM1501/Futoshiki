package util.constants;

import java.nio.file.Path;
import java.nio.file.Paths;

public class FileConstants {
    // Obtener el directorio actual de trabajo
    private static final String BASE_PATH = "data/";
    
    // Definir las rutas relativas de los archivos
    public static final String GAMES_FILE = BASE_PATH + "futoshiki2024partidas.xml";
    public static final String CONFIG_FILE = BASE_PATH + "futoshiki2024configuracion.xml";
    public static final String CURRENT_GAME_FILE = BASE_PATH + "futoshiki2024juegoactual.xml";
    public static final String TOP10_FILE = BASE_PATH + "futoshiki2024top10.xml";
    public static final String PLAYERS_FILE = BASE_PATH + "futoshiki2024players.xml";
    
    /**
     * Método para obtener la ruta absoluta de un archivo dado su ruta relativa.
     * 
     * @param relativePath La ruta relativa del archivo.
     * @return La ruta absoluta del archivo como una cadena de texto.
     */
    public static String getAbsolutePath(String relativePath) {
        return Paths.get(relativePath).toAbsolutePath().toString();
    }
}