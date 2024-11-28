package persistence;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import view.game.ScoreboardView;

import model.game.GameScore;
import util.constants.FileConstants;
import util.constants.GameConstants;

public class Top10Manager {
    private Map<String, List<GameScore>> scoresByLevel;
    private static Top10Manager instance;
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
        System.out.println("Intentando agregar score al Top 10:");
        System.out.println("Jugador: " + score.getPlayerName());
        System.out.println("Tiempo: " + score.getHours() + ":" + 
                          score.getMinutes() + ":" + score.getSeconds());
        System.out.println("Dificultad: " + score.getDifficulty());
        
        List<GameScore> levelScores = scoresByLevel.get(score.getDifficulty());
        System.out.println("Scores actuales en este nivel: " + levelScores.size());
        
        boolean added = false;
        // Si no hay 10 scores, agregar directamente
        if (levelScores.size() < GameConstants.TOP_10_SIZE) {
            levelScores.add(score);
            added = true;
        } else {
            // Si el nuevo score es mejor que el último
            GameScore lastScore = levelScores.get(levelScores.size() - 1);
            if (score.compareTo(lastScore) < 0) {
                levelScores.remove(lastScore);
                levelScores.add(score);
                added = true;
            }
        }
        
        if (added) {
            sortScores(levelScores);
            saveScores();
            // Notificar a la vista que hay nuevos scores
            ScoreboardView.getInstance().updateScores();
        }
        
        return added;
    }
    
    // Obtiene los scores de un nivel específico
    public List<GameScore> getScoresByLevel(String level) {
        return new ArrayList<>(scoresByLevel.get(level));
    }
    
    // Carga los scores desde el archivo
    private void loadScores() {
        try {
            File file = new File(FileConstants.TOP10_FILE);
            if (!file.exists()) return;

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();

            NodeList niveles = doc.getElementsByTagName("nivel");
            for (int i = 0; i < niveles.getLength(); i++) {
                Element nivelElement = (Element) niveles.item(i);
                String dificultad = nivelElement.getAttribute("dificultad");
                
                List<GameScore> levelScores = new ArrayList<>();
                NodeList scores = nivelElement.getElementsByTagName("score");
                
                for (int j = 0; j < scores.getLength(); j++) {
                    Element scoreElement = (Element) scores.item(j);
                    GameScore score = new GameScore(
                        scoreElement.getAttribute("jugador"),
                        Integer.parseInt(scoreElement.getAttribute("horas")),
                        Integer.parseInt(scoreElement.getAttribute("minutos")),
                        Integer.parseInt(scoreElement.getAttribute("segundos")),
                        dificultad,
                        Integer.parseInt(scoreElement.getAttribute("tamano"))
                    );
                    levelScores.add(score);
                }
                
                scoresByLevel.put(dificultad, levelScores);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Guarda los scores en el archivo
    private void saveScores() {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();

            // Elemento raíz
            Element rootElement = doc.createElement("top10");
            doc.appendChild(rootElement);

            // Por cada nivel de dificultad
            for (Map.Entry<String, List<GameScore>> entry : scoresByLevel.entrySet()) {
                Element levelElement = doc.createElement("nivel");
                levelElement.setAttribute("dificultad", entry.getKey());

                // Agregar cada score
                for (GameScore score : entry.getValue()) {
                    Element scoreElement = doc.createElement("score");
                    scoreElement.setAttribute("jugador", score.getPlayerName());
                    scoreElement.setAttribute("horas", String.valueOf(score.getHours()));
                    scoreElement.setAttribute("minutos", String.valueOf(score.getMinutes()));
                    scoreElement.setAttribute("segundos", String.valueOf(score.getSeconds()));
                    scoreElement.setAttribute("tamano", String.valueOf(score.getGridSize()));
                    
                    levelElement.appendChild(scoreElement);
                }
                
                rootElement.appendChild(levelElement);
            }

            // Escribir el documento
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(FileConstants.TOP10_FILE));
            transformer.transform(source, result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Ordena la lista de scores por tiempo (menor a mayor)
    private void sortScores(List<GameScore> scores) {
        Collections.sort(scores);
    }
    
    // Verifica si un tiempo calificaría para el Top 10
    public boolean wouldQualifyForTop10(String level, int totalSeconds) {
        System.out.println("Verificando calificación para Top 10:");
        System.out.println("Nivel: " + level);
        System.out.println("Tiempo total: " + totalSeconds + " segundos");
        
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

    public static Top10Manager getInstance() {
        if (instance == null) {
            instance = new Top10Manager();
        }
        return instance;
    }
}