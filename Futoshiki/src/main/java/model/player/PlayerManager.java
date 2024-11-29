package model.player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import model.game.GameScore;
import persistence.XMLPlayerManager;
import util.validators.InputValidator;
public class PlayerManager {
    private Map<String, Player> players;
    private Player currentPlayer;
    
    public PlayerManager() {
        // Cargar jugadores desde XML al inicializar
        players = XMLPlayerManager.loadPlayers();
    }
    
    // Registra un nuevo jugador
    // @return String con mensaje de error o null si se registró correctamente
    public String registerPlayer(String name, String password, String email) {
        // Validar inputs
        String nameError = InputValidator.validatePlayerName(name);
        if (nameError != null) return nameError;
        
        String passwordError = InputValidator.validatePassword(password);
        if (passwordError != null) return passwordError;
        
        String emailError = InputValidator.validateEmail(email);
        if (emailError != null) return emailError;
        
        // Verificar si el jugador ya existe
        if (players.containsKey(name)) {
            return "Ya existe un jugador con ese nombre";
        }
        
        // Crear y guardar nuevo jugador
        Player newPlayer = new Player(name, password, email);
        players.put(name, newPlayer);
        
        // Guardar inmediatamente en XML para persistir los cambios
        boolean saved = XMLPlayerManager.savePlayers(players);
        if (!saved) {
            return "Error al guardar el jugador";
        }
        
        return null;
    }
    
    // Inicia sesión de un jugador
    // @return String con mensaje de error o null si el login fue exitoso
    public String login(String name, String password) {
        System.out.println("PlayerManager: Verificando usuario " + name);
        System.out.println("Jugadores cargados: " + players.keySet());
        
        Player player = players.get(name);
        
        if (player == null) {
            System.out.println("PlayerManager: Usuario no encontrado");
            return "Jugador no encontrado";
        }
        
        System.out.println("PlayerManager: Usuario encontrado, verificando contraseña");
        
        if (!player.authenticate(password)) {
            System.out.println("PlayerManager: Contraseña incorrecta");
            return "Contraseña incorrecta";
        }
        
        System.out.println("PlayerManager: Login exitoso");
        currentPlayer = player;
        return null;
    }
    
    // Inicia el proceso de recuperación de contraseña
    // @return String con mensaje de error o null si se envió el email
    public String initiatePasswordRecovery(String name) {
        Player player = players.get(name);


        if (player == null) {
            return "Jugador no encontrado";
        }
        
        // Generar token de recuperación
        player.generateRecoveryToken();
        XMLPlayerManager.savePlayers(players);
        
        // En lugar de enviar email, mostrar el token
        EnvioEmail.sendEmail(player.getEmail(), player.getRecoveryToken());
        return "Tu token de recuperación es: " + player.getRecoveryToken();
    }
    
    // Completa el proceso de recuperación de contraseña
    public String completePasswordRecovery(String name, String token, String newPassword) {
        Player player = players.get(name);
        
        if (player == null) {
            return "Jugador no encontrado";
        }
        
        if (!player.validateRecoveryToken(token)) {
            return "Token de recuperación inválido";
        }
        
        String passwordError = InputValidator.validatePassword(newPassword);
        if (passwordError != null) return passwordError;
        
        player.changePassword(newPassword);
        XMLPlayerManager.savePlayers(players);
        
        return null;
    }
    
    // Agrega un nuevo score al jugador actual
    public void addScore(GameScore score) {
        if (currentPlayer != null) {
            currentPlayer.addScore(score);
            XMLPlayerManager.savePlayers(players);
        }
    }
    
    // Getters
    public Player getCurrentPlayer() {
        return currentPlayer;
    }
    
    public boolean isLoggedIn() {
        return currentPlayer != null;
    }
    
    public void logout() {
        currentPlayer = null;
    }
    
    // Obtiene una lista de todos los scores de un nivel específico
    public List<GameScore> getAllScoresForLevel(String difficulty) {
        List<GameScore> allScores = new ArrayList<>();
        for (Player player : players.values()) {
            for (GameScore score : player.getScores()) {
                if (score.getDifficulty().equals(difficulty)) {
                    allScores.add(score);
                }
            }
        }
        Collections.sort(allScores);
        return allScores;
    }
}