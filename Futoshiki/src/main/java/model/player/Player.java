package model.player;

import java.io.Serializable;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import model.game.GameScore;

public class Player implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String name;
    private String passwordHash;
    private String email;
    private List<GameScore> scores;
    private String recoveryToken; // Para recuperación de contraseña
    
    /**
     * Constructor de la clase Player.
     * 
     * @param name El nombre del jugador.
     * @param password La contraseña del jugador.
     * @param email El correo electrónico del jugador.
     */
    public Player(String name, String password, String email) {
        this.name = name;
        // Si el password ya es un hash (al cargar del XML), usarlo directamente
        if (password.length() == 64) { // SHA-256 produce un hash de 64 caracteres
            this.passwordHash = password;
        } else {
            this.passwordHash = hashPassword(password);
        }
        this.email = email;
        this.scores = new ArrayList<>();
    }
    
    // Métodos de autenticación

    /**
     * Autentica al jugador comparando el hash de la contraseña proporcionada.
     * 
     * @param password La contraseña a verificar.
     * @return true si la autenticación es exitosa, false en caso contrario.
     */
    public boolean authenticate(String password) {
        String inputHash = hashPassword(password);
        return this.passwordHash.equals(inputHash);
    }
    
    /**
     * Cambia la contraseña del jugador.
     * 
     * @param newPassword La nueva contraseña a establecer.
     */
    public void changePassword(String newPassword) {
        this.passwordHash = hashPassword(newPassword);
    }
    
    /**
     * Genera un token de recuperación único para el jugador.
     */
    public void generateRecoveryToken() {
        // Generar token único para recuperación
        this.recoveryToken = hashPassword(name + System.currentTimeMillis());
    }
    
    /**
     * Valida el token de recuperación proporcionado.
     * 
     * @param token El token a validar.
     * @return true si el token es válido, false en caso contrario.
     */
    public boolean validateRecoveryToken(String token) {
        return this.recoveryToken != null && this.recoveryToken.equals(token);
    }
    
    // Método para hash de contraseña

    /**
     * Genera un hash SHA-256 para la contraseña proporcionada.
     * 
     * @param password La contraseña a hashear.
     * @return El hash de la contraseña en formato hexadecimal.
     */
    private String hashPassword(String password) {
        try {
            // Usar UTF-8 para consistencia en el encoding
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes("UTF-8"));
            
            // Convertir bytes a hexadecimal de manera consistente
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    // Getters y setters

    /**
     * Obtiene el nombre del jugador.
     * 
     * @return El nombre del jugador.
     */
    public String getName() { return name; }

    /**
     * Obtiene el correo electrónico del jugador.
     * 
     * @return El correo electrónico del jugador.
     */
    public String getEmail() { return email; }

    /**
     * Obtiene la lista de puntuaciones del jugador.
     * 
     * @return La lista de puntuaciones del jugador.
     */
    public List<GameScore> getScores() { return scores; }

    /**
     * Obtiene el token de recuperación del jugador.
     * 
     * @return El token de recuperación del jugador.
     */
    public String getRecoveryToken() { return recoveryToken; }

    /**
     * Establece el token de recuperación del jugador.
     * 
     * @param recoveryToken El token de recuperación a establecer.
     */
    public void setRecoveryToken(String recoveryToken) { this.recoveryToken = recoveryToken; }

    /**
     * Obtiene el hash de la contraseña del jugador.
     * 
     * @return El hash de la contraseña del jugador.
     */
    public String getPasswordHash() { return passwordHash; }
    
    // Métodos para manejar scores

    /**
     * Añade una puntuación a la lista de puntuaciones del jugador.
     * 
     * @param score La puntuación a añadir.
     */
    public void addScore(GameScore score) {
        scores.add(score);
    }
}
