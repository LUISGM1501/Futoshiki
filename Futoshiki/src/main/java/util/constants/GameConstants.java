package util.constants;

public class GameConstants {
    // Niveles de dificultad
    /** Nivel de dificultad fácil */
    public static final String LEVEL_EASY = "Facil";
    /** Nivel de dificultad intermedio */
    public static final String LEVEL_MEDIUM = "Intermedio";
    /** Nivel de dificultad difícil */
    public static final String LEVEL_HARD = "Dificil";
    
    // Tipos de desigualdades
    /** Mayor en fila (>) */
    public static final String GREATER_ROW = "maf";    // mayor en fila (>)
    /** Menor en fila (<) */
    public static final String LESSER_ROW = "mef";     // menor en fila (<)
    /** Mayor en columna (>) */
    public static final String GREATER_COL = "mac";    // mayor en columna (>)
    /** Menor en columna (<) */
    public static final String LESSER_COL = "mec";     // menor en columna (<)
    
    // Símbolos visuales de desigualdades
    /** Símbolo de mayor (>) */
    public static final String SYMBOL_GREATER = ">";
    /** Símbolo de menor (<) */
    public static final String SYMBOL_LESSER = "<";
    /** Símbolo de mayor en columna (v) */
    public static final String SYMBOL_GREATER_COL = "v";
    /** Símbolo de menor en columna (^) */
    public static final String SYMBOL_LESSER_COL = "^";
    
    // Tipos de temporizador
    /** Temporizador tipo cronómetro */
    public static final String TIMER_CHRONOMETER = "chronometer";
    /** Temporizador tipo cuenta regresiva */
    public static final String TIMER_COUNTDOWN = "timer";
    /** Sin temporizador */
    public static final String TIMER_NONE = "none";
    
    // Posiciones del panel de dígitos
    /** Panel de dígitos a la derecha */
    public static final String PANEL_RIGHT = "right";
    /** Panel de dígitos a la izquierda */
    public static final String PANEL_LEFT = "left";
    
    // Valores por defecto
    /** Tamaño de cuadrícula por defecto */
    public static final int DEFAULT_GRID_SIZE = 5;
    /** Dificultad por defecto */
    public static final String DEFAULT_DIFFICULTY = LEVEL_EASY;
    /** Multinivel por defecto */
    public static final boolean DEFAULT_MULTILEVEL = false;
    /** Temporizador por defecto */
    public static final String DEFAULT_TIMER = TIMER_CHRONOMETER;
    /** Posición del panel por defecto */
    public static final String DEFAULT_PANEL_POSITION = PANEL_RIGHT;
    
    // Límites
    /** Tamaño mínimo de la cuadrícula */
    public static final int MIN_GRID_SIZE = 3;
    /** Tamaño máximo de la cuadrícula */
    public static final int MAX_GRID_SIZE = 10;
    /** Longitud máxima del nombre del jugador */
    public static final int MAX_PLAYER_NAME_LENGTH = 30;
    /** Tamaño del top 10 */
    public static final int TOP_10_SIZE = 10;
}