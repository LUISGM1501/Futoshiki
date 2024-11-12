package util.constants;

public class MessageConstants {
    // Mensajes de error de jugadas
    public static final String ERROR_DUPLICATE_ROW = 
        "JUGADA NO ES VÁLIDA PORQUE EL ELEMENTO YA ESTÁ EN LA FILA";
    public static final String ERROR_DUPLICATE_COLUMN = 
        "JUGADA NO ES VÁLIDA PORQUE EL ELEMENTO YA ESTÁ EN LA COLUMNA";
    public static final String ERROR_GREATER_CONSTRAINT = 
        "JUGADA NO ES VÁLIDA PORQUE NO CUMPLE CON LA RESTRICCIÓN DE MAYOR";
    public static final String ERROR_LESSER_CONSTRAINT = 
        "JUGADA NO ES VÁLIDA PORQUE NO CUMPLE CON LA RESTRICCIÓN DE MENOR";
    public static final String ERROR_CONSTANT_CELL = 
        "NO SE PUEDE MODIFICAR UNA CELDA CONSTANTE";
    public static final String ERROR_NO_DIGIT_SELECTED = 
        "FALTA QUE SELECCIONE UN DÍGITO";
        
    // Mensajes de confirmación
    public static final String CONFIRM_CLEAR_GAME = 
        "¿ESTÁ SEGURO DE BORRAR EL JUEGO?";
    public static final String CONFIRM_END_GAME = 
        "¿ESTÁ SEGURO DE TERMINAR EL JUEGO?";
    public static final String CONFIRM_TIMER_EXPIRED = 
        "TIEMPO EXPIRADO. ¿DESEA CONTINUAR EL MISMO JUEGO?";
        
    // Mensajes de éxito/información
    public static final String SUCCESS_GAME_COMPLETED = 
        "¡EXCELENTE! JUEGO TERMINADO CON ÉXITO";
    public static final String INFO_NO_MORE_MOVES = 
        "NO HAY MAS JUGADAS PARA BORRAR";
    public static final String INFO_NO_GAMES_FOR_LEVEL = 
        "NO HAY PARTIDAS PARA ESTE NIVEL";
        
    // Mensajes de configuración
    public static final String ERROR_INVALID_TIME = 
        "Los valores del temporizador no son válidos";
    public static final String ERROR_PLAYER_EXISTS = 
        "Ya existe un jugador con ese nombre";
    public static final String ERROR_INVALID_PASSWORD = 
        "La contraseña debe tener al menos 6 caracteres y contener letras y números";
    
    // Títulos de ventanas/diálogos
    public static final String TITLE_CONFIGURATION = "Configuración";
    public static final String TITLE_TOP_10 = "Top 10";
    public static final String TITLE_HELP = "Ayuda";
    public static final String TITLE_ABOUT = "Acerca de";
    public static final String TITLE_ERROR = "Error";
    public static final String TITLE_SUCCESS = "Éxito";
    public static final String TITLE_CONFIRM = "Confirmar";
}