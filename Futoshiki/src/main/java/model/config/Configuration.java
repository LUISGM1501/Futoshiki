package model.config;

public class Configuration {
    private int gridSize;
    private String difficulty;
    private boolean isMultiLevel;
    private String timerType; // "chronometer", "none", "timer"
    private int timerHours;
    private int timerMinutes;
    private int timerSeconds;
    private String digitPanelPosition; // "right" or "left"
    private String playerName;

    /**
     * Constructor por defecto que inicializa la configuración con valores predeterminados.
     */
    public Configuration() {
        // Valores por defecto según especificación
        this.gridSize = 5;
        this.difficulty = "Facil";
        this.isMultiLevel = false;
        this.timerType = "chronometer";
        this.timerHours = 0;
        this.timerMinutes = 0;
        this.timerSeconds = 0;
        this.digitPanelPosition = "right";
        this.playerName = "";
    }

    // Getters y setters

    /**
     * Obtiene el tamaño de la cuadrícula.
     * 
     * @return el tamaño de la cuadrícula.
     */
    public int getGridSize() {
        return gridSize;
    }

    /**
     * Establece el tamaño de la cuadrícula.
     * 
     * @param gridSize el tamaño de la cuadrícula a establecer.
     */
    public void setGridSize(int gridSize) {
        this.gridSize = gridSize;
    }

    /**
     * Obtiene la dificultad del juego.
     * 
     * @return la dificultad del juego.
     */
    public String getDifficulty() {
        return difficulty;
    }

    /**
     * Establece la dificultad del juego.
     * 
     * @param difficulty la dificultad del juego a establecer.
     */
    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * Verifica si el juego es de múltiples niveles.
     * 
     * @return true si es de múltiples niveles, false en caso contrario.
     */
    public boolean isMultiLevel() {
        return isMultiLevel;
    }

    /**
     * Establece si el juego es de múltiples niveles.
     * 
     * @param isMultiLevel true para múltiples niveles, false en caso contrario.
     */
    public void setMultiLevel(boolean isMultiLevel) {
        this.isMultiLevel = isMultiLevel;
    }

    /**
     * Obtiene el tipo de temporizador.
     * 
     * @return el tipo de temporizador.
     */
    public String getTimerType() {
        return timerType;
    }

    /**
     * Establece el tipo de temporizador.
     * 
     * @param timerType el tipo de temporizador a establecer.
     */
    public void setTimerType(String timerType) {
        this.timerType = timerType;
    }

    /**
     * Obtiene las horas del temporizador.
     * 
     * @return las horas del temporizador.
     */
    public int getTimerHours() {
        return timerHours;
    }

    /**
     * Establece las horas del temporizador.
     * 
     * @param timerHours las horas del temporizador a establecer.
     */
    public void setTimerHours(int timerHours) {
        this.timerHours = timerHours;
    }

    /**
     * Obtiene los minutos del temporizador.
     * 
     * @return los minutos del temporizador.
     */
    public int getTimerMinutes() {
        return timerMinutes;
    }

    /**
     * Establece los minutos del temporizador.
     * 
     * @param timerMinutes los minutos del temporizador a establecer.
     */
    public void setTimerMinutes(int timerMinutes) {
        this.timerMinutes = timerMinutes;
    }

    /**
     * Obtiene los segundos del temporizador.
     * 
     * @return los segundos del temporizador.
     */
    public int getTimerSeconds() {
        return timerSeconds;
    }

    /**
     * Establece los segundos del temporizador.
     * 
     * @param timerSeconds los segundos del temporizador a establecer.
     */
    public void setTimerSeconds(int timerSeconds) {
        this.timerSeconds = timerSeconds;
    }

    /**
     * Obtiene la posición del panel de dígitos.
     * 
     * @return la posición del panel de dígitos.
     */
    public String getDigitPanelPosition() {
        return digitPanelPosition;
    }

    /**
     * Establece la posición del panel de dígitos.
     * 
     * @param digitPanelPosition la posición del panel de dígitos a establecer.
     */
    public void setDigitPanelPosition(String digitPanelPosition) {
        this.digitPanelPosition = digitPanelPosition;
    }

    /**
     * Obtiene el nombre del jugador.
     * 
     * @return el nombre del jugador.
     */
    public String getPlayerName() {
        return playerName;
    }
    
    /**
     * Establece el nombre del jugador.
     * 
     * @param playerName el nombre del jugador a establecer.
     */
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}