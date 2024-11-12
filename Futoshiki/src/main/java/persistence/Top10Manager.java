package persistence;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.game.GameScore;
import util.constants.FileConstants;
import util.constants.GameConstants;

public class Top10Manager {
    private Map<String, List<GameScore>> scoresByLevel;
    
    public Top10Manager() {
        scoresByLevel = new HashMap<>();
        scoresByLevel.put(GameConstants.LEVEL_EASY, new ArrayList<>());
        scoresByLevel.put(GameConstants.LEVEL_MEDIUM, new ArrayList<>());
        scoresByLevel.put(GameConstants.LEVEL_HARD, new ArrayList<>());
        loadScores();
    }
    
    // Intenta agregar un nuevo score al Top 10
    // @return true si el score fue agregado (entró al top 10)
    public boolean addScore(GameScore score) {
        List<GameScore> levelScores = scoresByLevel.get(score.getDifficulty());
        
        // Si no hay 10 scores, agregar directamente
        if (levelScores.size() < GameConstants.TOP_10_SIZE) {
            levelScores.add(score);
            sortScores(levelScores);
            saveScores();
            return true;
        }
        
        // Si el nuevo score es mejor que el último
        GameScore lastScore = levelScores.get(levelScores.size() - 1);
        if (score.compareTo(lastScore) < 0) {
            levelScores.remove(lastScore);
            levelScores.add(score);
            sortScores(levelScores);
            saveScores();
            return true;
        }
        
        return false;
    }
    
    // Obtiene los scores de un nivel específico
    public List<GameScore> getScoresByLevel(String level) {
        return new ArrayList<>(scoresByLevel.get(level));
    }
    
    // Carga los scores desde el archivo
    private void loadScores() {
        try (ObjectInputStream in = new ObjectInputStream(
                new FileInputStream(FileConstants.TOP10_FILE))) {
            
            scoresByLevel = (Map<String, List<GameScore>>) in.readObject();
            
        } catch (IOException | ClassNotFoundException e) {
            // Si hay error mantener las listas vacías
        }
    }
    
    // Guarda los scores en el archivo
    private void saveScores() {
        try (ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream(FileConstants.TOP10_FILE))) {
            
            out.writeObject(scoresByLevel);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // Ordena la lista de scores por tiempo (menor a mayor)
    private void sortScores(List<GameScore> scores) {
        Collections.sort(scores);
    }
    
    // Verifica si un tiempo calificaría para el Top 10
    public boolean wouldQualifyForTop10(String level, int totalSeconds) {
        List<GameScore> levelScores = scoresByLevel.get(level);
        
        // Si no hay 10 scores, cualquier tiempo califica
        if (levelScores.size() < GameConstants.TOP_10_SIZE) {
            return true;
        }
        
        // Comparar con el último score
        GameScore lastScore = levelScores.get(levelScores.size() - 1);
        int lastScoreSeconds = lastScore.getHours() * 3600 + 
                             lastScore.getMinutes() * 60 + 
                             lastScore.getSeconds();
        
        return totalSeconds < lastScoreSeconds;
    }
}