package util.validators;

public class TimeValidator {
    private static final int MAX_HOURS = 5;
    private static final int MAX_MINUTES = 59;
    private static final int MAX_SECONDS = 59;

    // Valida los valores del temporizador según las restricciones del juego
    // @return String con mensaje de error o null si es válido
    public static String validateTimerValues(int hours, int minutes, int seconds) {
        // Validar rangos
        if (hours < 0 || hours > MAX_HOURS) {
            return "Las horas deben estar entre 0 y " + MAX_HOURS;
        }
        
        if (minutes < 0 || minutes > MAX_MINUTES) {
            return "Los minutos deben estar entre 0 y " + MAX_MINUTES;
        }
        
        if (seconds < 0 || seconds > MAX_SECONDS) {
            return "Los segundos deben estar entre 0 y " + MAX_SECONDS;
        }
        
        // Validar que al menos uno de los valores sea mayor que cero
        if (hours == 0 && minutes == 0 && seconds == 0) {
            return "El temporizador debe tener al menos un valor mayor que cero";
        }
        
        return null;
    }
    
    // Convierte el tiempo a segundos totales
    public static int convertToSeconds(int hours, int minutes, int seconds) {
        return hours * 3600 + minutes * 60 + seconds;
    }
    
    // Convierte segundos totales a formato HH:MM:SS
    public static String formatTime(int totalSeconds) {
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;
        
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
    
    // Calcula el tiempo transcurrido entre dos marcas de tiempo
    public static int calculateElapsedTime(long startTime, long endTime) {
        return (int)((endTime - startTime) / 1000);
    }
    
    // Calcula el tiempo restante
    public static int calculateRemainingTime(int initialSeconds, int elapsedSeconds) {
        return Math.max(0, initialSeconds - elapsedSeconds);
    }
}