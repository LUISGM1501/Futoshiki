package util.constants;

public class MessageConstants {
    // Mensajes de error de jugadas
    /** Mensaje de error cuando el elemento ya está en la fila */
    public static final String ERROR_DUPLICATE_ROW = 
        "JUGADA NO ES VÁLIDA PORQUE EL ELEMENTO YA ESTÁ EN LA FILA";
    /** Mensaje de error cuando el elemento ya está en la columna */
    public static final String ERROR_DUPLICATE_COLUMN = 
        "JUGADA NO ES VÁLIDA PORQUE EL ELEMENTO YA ESTÁ EN LA COLUMNA";
    /** Mensaje de error cuando no se cumple la restricción de mayor */
    public static final String ERROR_GREATER_CONSTRAINT = 
        "JUGADA NO ES VÁLIDA PORQUE NO CUMPLE CON LA RESTRICCIÓN DE MAYOR";
    /** Mensaje de error cuando no se cumple la restricción de menor */
    public static final String ERROR_LESSER_CONSTRAINT = 
        "JUGADA NO ES VÁLIDA PORQUE NO CUMPLE CON LA RESTRICCIÓN DE MENOR";
    /** Mensaje de error cuando se intenta modificar una celda constante */
    public static final String ERROR_CONSTANT_CELL = 
        "NO SE PUEDE MODIFICAR UNA CELDA CONSTANTE";
    /** Mensaje de error cuando no se ha seleccionado un dígito */
    public static final String ERROR_NO_DIGIT_SELECTED = 
        "FALTA QUE SELECCIONE UN DÍGITO";
        
    // Mensajes de confirmación
    /** Mensaje de confirmación para borrar el juego */
    public static final String CONFIRM_CLEAR_GAME = 
        "¿ESTÁ SEGURO DE BORRAR EL JUEGO?";
    /** Mensaje de confirmación para terminar el juego */
    public static final String CONFIRM_END_GAME = 
        "¿ESTÁ SEGURO DE TERMINAR EL JUEGO?";
    /** Mensaje de confirmación cuando el tiempo ha expirado */
    public static final String CONFIRM_TIMER_EXPIRED = 
        "TIEMPO EXPIRADO. ¿DESEA CONTINUAR EL MISMO JUEGO?";
        
    // Mensajes de éxito/información
    /** Mensaje de éxito cuando el juego se completa */
    public static final String SUCCESS_GAME_COMPLETED = 
        "¡EXCELENTE! JUEGO TERMINADO CON ÉXITO";
    /** Mensaje informativo cuando no hay más jugadas para borrar */
    public static final String INFO_NO_MORE_MOVES = 
        "NO HAY MAS JUGADAS PARA BORRAR";
    /** Mensaje informativo cuando no hay partidas para el nivel */
    public static final String INFO_NO_GAMES_FOR_LEVEL = 
        "NO HAY PARTIDAS PARA ESTE NIVEL";
        
    // Mensajes de configuración
    /** Mensaje de error cuando los valores del temporizador no son válidos */
    public static final String ERROR_INVALID_TIME = 
        "Los valores del temporizador no son válidos";
    /** Mensaje de error cuando ya existe un jugador con ese nombre */
    public static final String ERROR_PLAYER_EXISTS = 
        "Ya existe un jugador con ese nombre";
    /** Mensaje de error cuando la contraseña no cumple con los requisitos */
    public static final String ERROR_INVALID_PASSWORD = 
        "La contraseña debe tener al menos 6 caracteres y contener letras y números";
    
    // Títulos de ventanas/diálogos
    /** Título para la ventana de configuración */
    public static final String TITLE_CONFIGURATION = "Configuración";
    /** Título para la ventana del Top 10 */
    public static final String TITLE_TOP_10 = "Top 10";
    /** Título para la ventana de ayuda */
    public static final String TITLE_HELP = "Ayuda";
    /** Título para la ventana de acerca de */
    public static final String TITLE_ABOUT = "Acerca de";
    /** Título para la ventana de error */
    public static final String TITLE_ERROR = "Error";
    /** Título para la ventana de éxito */
    public static final String TITLE_SUCCESS = "Éxito";
    /** Título para la ventana de confirmación */
    public static final String TITLE_CONFIRM = "Confirmar";
}