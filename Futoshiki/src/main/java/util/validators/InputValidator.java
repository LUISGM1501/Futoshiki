package util.validators;

import model.config.Configuration;

public class InputValidator {
    private static final int MIN_GRID_SIZE = 3;
    private static final int MAX_GRID_SIZE = 10;
    private static final int MAX_PLAYER_NAME_LENGTH = 30;
    
    // Valida el tamaño de la cuadrícula
    /**
     * Valida que el tamaño de la cuadrícula esté dentro de los límites permitidos.
     * 
     * @param size El tamaño de la cuadrícula a validar.
     * @return Un mensaje de error si el tamaño es inválido, o null si es válido.
     */
    public static String validateGridSize(int size) {
        if (size < MIN_GRID_SIZE || size > MAX_GRID_SIZE) {
            return "El tamaño de la cuadrícula debe estar entre " + MIN_GRID_SIZE + " y " + MAX_GRID_SIZE;
        }
        return null;
    }
    
    // Valida el nivel de dificultad
    /**
     * Valida que el nivel de dificultad sea uno de los valores permitidos.
     * 
     * @param difficulty El nivel de dificultad a validar.
     * @return Un mensaje de error si el nivel es inválido, o null si es válido.
     */
    public static String validateDifficulty(String difficulty) {
        if (difficulty == null || difficulty.trim().isEmpty()) {
            return "Debe seleccionar un nivel de dificultad";
        }
        
        switch (difficulty.toLowerCase()) {
            case "facil":
            case "intermedio":
            case "dificil":
                return null;
            default:
                return "Nivel de dificultad inválido";
        }
    }
    
    // Valida el nombre del jugador
    /**
     * Valida que el nombre del jugador cumpla con los requisitos de longitud y caracteres permitidos.
     * 
     * @param name El nombre del jugador a validar.
     * @return Un mensaje de error si el nombre es inválido, o null si es válido.
     */
    public static String validatePlayerName(String name) {
        if (name == null) {
            return "El nombre no puede ser null";
        }

        // No puede ser el nombre de un jugador invitado
        if (name.equals("Invitado")) {
            return "El nombre de jugador invitado no es válido";
        }
        
        // Nombre vacío está permitido (jugador incógnito)
        if (name.isEmpty()) {
            return null;
        }
        
        if (name.length() > MAX_PLAYER_NAME_LENGTH) {
            return "El nombre no puede tener más de " + MAX_PLAYER_NAME_LENGTH + " caracteres";
        }
        
        // Validar caracteres válidos (letras, números y espacios)
        if (!name.matches("^[a-zA-Z0-9 ]+$")) {
            return "El nombre solo puede contener letras, números y espacios";
        }
        
        return null;
    }
    
    // Valida la contraseña del jugador
    /**
     * Valida que la contraseña cumpla con los requisitos mínimos de longitud y contenido.
     * 
     * @param password La contraseña a validar.
     * @return Un mensaje de error si la contraseña es inválida, o null si es válida.
     */
    public static String validatePassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            return "La contraseña no puede estar vacía";
        }
        
        if (password.length() < 6) {
            return "La contraseña debe tener al menos 6 caracteres";
        }
        
        // Validar que tenga al menos una letra y un número
        boolean hasLetter = false;
        boolean hasNumber = false;
        
        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) hasLetter = true;
            if (Character.isDigit(c)) hasNumber = true;
        }
        
        if (!hasLetter || !hasNumber) {
            return "La contraseña debe contener al menos una letra y un número";
        }
        
        return null;
    }
    
    // Valida el email para recuperación de contraseña
    /**
     * Valida que el email tenga un formato correcto.
     * 
     * @param email El email a validar.
     * @return Un mensaje de error si el email es inválido, o null si es válido.
     */
    public static String validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return "El email no puede estar vacío";
        }
        
        // Validación básica de formato de email
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            return "Formato de email inválido";
        }
        
        return null;
    }
    
    // Valida la configuración del juego
    /**
     * Valida que la configuración del juego sea correcta.
     * 
     * @param config La configuración del juego a validar.
     * @return Un mensaje de error si la configuración es inválida, o null si es válida.
     */
    public static String validateConfiguration(Configuration config) {
        // Validar tamaño de cuadrícula
        String gridSizeError = validateGridSize(config.getGridSize());
        if (gridSizeError != null) return gridSizeError;
        
        // Validar dificultad
        String difficultyError = validateDifficulty(config.getDifficulty());
        if (difficultyError != null) return difficultyError;
        
        // Validar temporizador si está activo
        if (config.getTimerType().equals("timer")) {
            String timerError = TimeValidator.validateTimerValues(
                config.getTimerHours(),
                config.getTimerMinutes(),
                config.getTimerSeconds()
            );
            if (timerError != null) return timerError;
        }
        
        // Validar nombre del jugador
        String nameError = validatePlayerName(config.getPlayerName());
        if (nameError != null) return nameError;
        
        return null;
    }
}