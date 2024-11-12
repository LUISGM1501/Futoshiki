package model.player;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
    
    public Player(String name, String password, String email) {
        this.name = name;
        this.passwordHash = hashPassword(password);
        this.email = email;
        this.scores = new ArrayList<>();
    }
    
    // Métodos de autenticación
    public boolean authenticate(String password) {
        return this.passwordHash.equals(hashPassword(password));
    }
    
    public void changePassword(String newPassword) {
        this.passwordHash = hashPassword(newPassword);
    }
    
    public void generateRecoveryToken() {
        // Generar token único para recuperación
        this.recoveryToken = hashPassword(name + System.currentTimeMillis());
    }
    
    public boolean validateRecoveryToken(String token) {
        return this.recoveryToken != null && this.recoveryToken.equals(token);
    }
    
    // Método para hash de contraseña
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            
            // Convertir bytes a hexadecimal
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
            
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    // Getters y setters
    public String getName() { return name; }
    public String getEmail() { return email; }
    public List<GameScore> getScores() { return scores; }
    public String getRecoveryToken() { return recoveryToken; }
    public void setRecoveryToken(String recoveryToken) { this.recoveryToken = recoveryToken; }
    public String getPasswordHash() { return passwordHash; }
    
    // Métodos para manejar scores
    public void addScore(GameScore score) {
        scores.add(score);
    }
}
