package util.constants;

public class GameConstants {
    // Niveles de dificultad
    public static final String LEVEL_EASY = "Facil";
    public static final String LEVEL_MEDIUM = "Intermedio";
    public static final String LEVEL_HARD = "Dificil";
    
    // Tipos de desigualdades
    public static final String GREATER_ROW = "maf";    // mayor en fila (>)
    public static final String LESSER_ROW = "mef";     // menor en fila (<)
    public static final String GREATER_COL = "mac";    // mayor en columna (>)
    public static final String LESSER_COL = "mec";     // menor en columna (<)
    
    // Símbolos visuales de desigualdades
    public static final String SYMBOL_GREATER = ">";
    public static final String SYMBOL_LESSER = "<";
    public static final String SYMBOL_GREATER_COL = "v";
    public static final String SYMBOL_LESSER_COL = "^";
    
    // Tipos de temporizador
    public static final String TIMER_CHRONOMETER = "chronometer";
    public static final String TIMER_COUNTDOWN = "timer";
    public static final String TIMER_NONE = "none";
    
    // Posiciones del panel de dígitos
    public static final String PANEL_RIGHT = "right";
    public static final String PANEL_LEFT = "left";
    
    // Valores por defecto
    public static final int DEFAULT_GRID_SIZE = 5;
    public static final String DEFAULT_DIFFICULTY = LEVEL_EASY;
    public static final boolean DEFAULT_MULTILEVEL = false;
    public static final String DEFAULT_TIMER = TIMER_CHRONOMETER;
    public static final String DEFAULT_PANEL_POSITION = PANEL_RIGHT;
    
    // Límites
    public static final int MIN_GRID_SIZE = 3;
    public static final int MAX_GRID_SIZE = 10;
    public static final int MAX_PLAYER_NAME_LENGTH = 30;
    public static final int TOP_10_SIZE = 10;
}