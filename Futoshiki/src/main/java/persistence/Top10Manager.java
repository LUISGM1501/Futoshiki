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

    /**
     * Constructor de Top10Manager.
     * Inicializa el mapa de scores por nivel y carga los scores desde el archivo.
     */
    public Top10Manager() {
        scoresByLevel = new HashMap<>();
        scoresByLevel.put(GameConstants.LEVEL_EASY, new ArrayList<>());
        scoresByLevel.put(GameConstants.LEVEL_MEDIUM, new ArrayList<>());
        scoresByLevel.put(GameConstants.LEVEL_HARD, new ArrayList<>());
        loadScores();
    }
    
    /**
     * Intenta agregar un nuevo score al Top 10.
     * 
     * @param score El score a agregar.
     * @return true si el score fue agregado (entró al top 10), false en caso contrario.
     */
    public boolean addScore(GameScore score) {
        System.out.println("\n=== AÑADIENDO NUEVO SCORE ===");
        System.out.println("Score a añadir:");
        System.out.println("- Jugador: " + score.getPlayerName());
        System.out.println("- Tiempo: " + score.getHours() + ":" + 
                          score.getMinutes() + ":" + score.getSeconds());
        System.out.println("- Dificultad: " + score.getDifficulty());
        System.out.println("- Tamaño: " + score.getGridSize());

        try {
            // Cargar scores existentes primero
            Document doc;
            Element rootElement;
            File file = new File(FileConstants.TOP10_FILE);
            
            if (file.exists()) {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                doc = dBuilder.parse(file);
                rootElement = doc.getDocumentElement();
            } else {
                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
                doc = docBuilder.newDocument();
                rootElement = doc.createElement("top10");
                doc.appendChild(rootElement);
            }

            // Buscar o crear el elemento del nivel
            Element nivelElement = null;
            NodeList niveles = doc.getElementsByTagName("nivel");
            for (int i = 0; i < niveles.getLength(); i++) {
                Element nivel = (Element) niveles.item(i);
                if (nivel.getAttribute("dificultad").equals(score.getDifficulty())) {
                    nivelElement = nivel;
                    break;
                }
            }

            if (nivelElement == null) {
                nivelElement = doc.createElement("nivel");
                nivelElement.setAttribute("dificultad", score.getDifficulty());
                rootElement.appendChild(nivelElement);
            }

            // Crear nuevo elemento score
            Element scoreElement = doc.createElement("score");
            scoreElement.setAttribute("jugador", score.getPlayerName());
            scoreElement.setAttribute("horas", String.valueOf(score.getHours()));
            scoreElement.setAttribute("minutos", String.valueOf(score.getMinutes()));
            scoreElement.setAttribute("segundos", String.valueOf(score.getSeconds()));
            scoreElement.setAttribute("tamano", String.valueOf(score.getGridSize()));

            // Añadir nuevo score
            nivelElement.appendChild(scoreElement);

            // Ordenar y mantener solo los mejores 10 scores por nivel y tamaño
            List<Element> scoreElements = new ArrayList<>();
            NodeList scores = nivelElement.getElementsByTagName("score");
            for (int i = 0; i < scores.getLength(); i++) {
                scoreElements.add((Element) scores.item(i));
            }

            // Ordenar por tiempo y filtrar por tamaño
            final int finalSize = score.getGridSize();
            scoreElements.removeIf(e -> Integer.parseInt(e.getAttribute("tamano")) != finalSize);
            scoreElements.sort((a, b) -> compareScores(a, b));

            // Mantener solo los mejores 10
            while (scoreElements.size() > 10) {
                Element lastScore = scoreElements.remove(scoreElements.size() - 1);
                nivelElement.removeChild(lastScore);
            }

            // Guardar el documento
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(file);
            transformer.transform(source, result);
            
            System.out.println("Score guardado exitosamente en " + file.getAbsolutePath());
            return true;
            
        } catch (Exception e) {
            System.err.println("Error al guardar el score: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private int compareScores(Element a, Element b) {
        int hoursA = Integer.parseInt(a.getAttribute("horas"));
        int minutesA = Integer.parseInt(a.getAttribute("minutos"));
        int secondsA = Integer.parseInt(a.getAttribute("segundos"));
        
        int hoursB = Integer.parseInt(b.getAttribute("horas"));
        int minutesB = Integer.parseInt(b.getAttribute("minutos"));
        int secondsB = Integer.parseInt(b.getAttribute("segundos"));
        
        int totalSecondsA = hoursA * 3600 + minutesA * 60 + secondsA;
        int totalSecondsB = hoursB * 3600 + minutesB * 60 + secondsB;
        
        return Integer.compare(totalSecondsA, totalSecondsB);
    }
    
    /**
     * Obtiene los scores de un nivel específico.
     * 
     * @param level El nivel de dificultad.
     * @return Una lista de scores para el nivel especificado.
     */
    public List<GameScore> getScoresByLevel(String level) {
        return new ArrayList<>(scoresByLevel.get(level));
    }
    
    /**
     * Obtiene los scores de un nivel y tamaño específico.
     * 
     * @param difficulty La dificultad del nivel.
     * @param size El tamaño del tablero.
     * @return Una lista de scores para el nivel y tamaño especificado.
     */
    public List<GameScore> getScoresByLevelAndSize(String difficulty, int size) {
        List<GameScore> allScores = new ArrayList<>();
        for (GameScore score : scoresByLevel.get(difficulty)) {
            if (score.getGridSize() == size) {
                allScores.add(score);
            }
        }
        Collections.sort(allScores);
        return allScores.size() > GameConstants.TOP_10_SIZE ? allScores.subList(0, GameConstants.TOP_10_SIZE) : allScores;
    }

    /**
     * Carga los scores desde el archivo.
     */
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
    
    /**
     * Guarda los scores en el archivo.
     */
    private boolean saveScores() {
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

            System.out.println("Guardando scores en: " + FileConstants.TOP10_FILE);
            return true;
        } catch (Exception e) {
            System.err.println("Error al guardar scores: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Ordena la lista de scores por tiempo (menor a mayor).
     * 
     * @param scores La lista de scores a ordenar.
     */
    private void sortScores(List<GameScore> scores) {
        Collections.sort(scores);
    }
    
    /**
     * Verifica si un tiempo calificaría para el Top 10.
     * 
     * @param level El nivel de dificultad.
     * @param totalSeconds El tiempo total en segundos.
     * @param size El tamaño del tablero.
     * @return true si el tiempo calificaría para el Top 10, false en caso contrario.
     */
    public boolean wouldQualifyForTop10(String level, int totalSeconds, int size) {
        System.out.println("\n=== VERIFICACIÓN DE CALIFICACIÓN TOP 10 ===");
        System.out.println("Verificando para:");
        System.out.println("- Dificultad: " + level);
        System.out.println("- Tiempo: " + totalSeconds + " segundos");
        System.out.println("- Tamaño: " + size);

        List<GameScore> levelScores = getScoresByLevelAndSize(level, size);
        System.out.println("Scores actuales para esta categoría: " + levelScores.size());

        // Si no hay 10 scores, cualquier tiempo califica
        if (levelScores.size() < GameConstants.TOP_10_SIZE) {
            System.out.println("Califica automáticamente - Menos de 10 scores");
            return true;
        }
        
        // Comparar con el último score
        GameScore lastScore = levelScores.get(levelScores.size() - 1);
        int lastScoreSeconds = lastScore.getHours() * 3600 + 
                             lastScore.getMinutes() * 60 + 
                             lastScore.getSeconds();
        
        System.out.println("Último tiempo en top 10: " + lastScoreSeconds + " segundos");
        System.out.println("¿Nuevo tiempo califica?: " + (totalSeconds < lastScoreSeconds));
        
        return totalSeconds < lastScoreSeconds;
    }

    /**
     * Obtiene la instancia única de Top10Manager.
     * 
     * @return La instancia de Top10Manager.
     */
    public static Top10Manager getInstance() {
        if (instance == null) {
            instance = new Top10Manager();
        }
        return instance;
    }
}