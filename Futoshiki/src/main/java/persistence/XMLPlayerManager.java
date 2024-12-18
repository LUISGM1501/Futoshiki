package persistence;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import model.game.GameScore;
import model.player.Player;
import util.constants.FileConstants;

public class XMLPlayerManager {
    private static final String PLAYERS_FILE = FileConstants.PLAYERS_FILE;
    
    /**
     * Guarda la lista de jugadores en un archivo XML.
     * 
     * @param players Un mapa que asocia nombres de jugadores con sus objetos Player.
     * @return true si los jugadores se guardaron correctamente, false en caso de error.
     */
    public static boolean savePlayers(Map<String, Player> players) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();
            
            // Elemento raíz
            Element rootElement = doc.createElement("jugadores");
            doc.appendChild(rootElement);
            
            // Agregar cada jugador
            for (Player player : players.values()) {
                Element playerElement = doc.createElement("jugador");
                
                Element nameElement = doc.createElement("nombre");
                nameElement.setTextContent(player.getName());
                playerElement.appendChild(nameElement);
                
                Element passwordElement = doc.createElement("password");
                passwordElement.setTextContent(player.getPasswordHash());
                playerElement.appendChild(passwordElement);
                
                Element emailElement = doc.createElement("email");
                emailElement.setTextContent(player.getEmail());
                playerElement.appendChild(emailElement);
                
                if (player.getRecoveryToken() != null) {
                    Element tokenElement = doc.createElement("token");
                    tokenElement.setTextContent(player.getRecoveryToken());
                    playerElement.appendChild(tokenElement);
                }
                
                // Agregar scores
                Element scoresElement = doc.createElement("scores");
                for (GameScore score : player.getScores()) {
                    Element scoreElement = doc.createElement("score");
                    scoreElement.setAttribute("difficulty", score.getDifficulty());
                    scoreElement.setAttribute("hours", String.valueOf(score.getHours()));
                    scoreElement.setAttribute("minutes", String.valueOf(score.getMinutes()));
                    scoreElement.setAttribute("seconds", String.valueOf(score.getSeconds()));
                    scoresElement.appendChild(scoreElement);
                }
                playerElement.appendChild(scoresElement);
                
                rootElement.appendChild(playerElement);
            }
            
            // Escribir al archivo
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(PLAYERS_FILE));
            transformer.transform(source, result);
            
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Carga la lista de jugadores desde un archivo XML.
     * 
     * @return Un mapa que asocia nombres de jugadores con sus objetos Player.
     */
    public static Map<String, Player> loadPlayers() {
        Map<String, Player> players = new HashMap<>();
        
        try {
            File file = new File(PLAYERS_FILE);
            System.out.println("XMLPlayerManager: Cargando jugadores desde " + file.getAbsolutePath());
            
            if (!file.exists()) {
                System.out.println("XMLPlayerManager: Archivo de jugadores no existe");
                return players;
            }
            
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();

            NodeList playerNodes = doc.getElementsByTagName("jugador");
            
            for (int i = 0; i < playerNodes.getLength(); i++) {
                Node playerNode = playerNodes.item(i);
                
                if (playerNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element playerElement = (Element) playerNode;
                    
                    String name = getElementContent(playerElement, "nombre");
                    String passwordHash = getElementContent(playerElement, "password");
                    String email = getElementContent(playerElement, "email");
                    String token = getElementContent(playerElement, "token");

                    System.out.println("XMLPlayerManager: Jugador encontrado - " + name);
                    System.out.println("XMLPlayerManager: Contraseña hash - " + passwordHash);
                    System.out.println("XMLPlayerManager: Email - " + email);
                    System.out.println("XMLPlayerManager: Token - " + token);
                    
                    Player player = new Player(name, passwordHash, email);
                    if (token != null && !token.isEmpty()) {
                        player.setRecoveryToken(token);
                    }
                    
                    // Cargar scores
                    NodeList scoreNodes = playerElement.getElementsByTagName("score");
                    for (int j = 0; j < scoreNodes.getLength(); j++) {
                        Element scoreElement = (Element) scoreNodes.item(j);
                        String difficulty = scoreElement.getAttribute("difficulty");
                        int hours = Integer.parseInt(scoreElement.getAttribute("hours"));
                        int minutes = Integer.parseInt(scoreElement.getAttribute("minutes"));
                        int seconds = Integer.parseInt(scoreElement.getAttribute("seconds"));
                        
                        GameScore score = new GameScore(name, hours, minutes, seconds, difficulty, 5);
                        player.addScore(score);
                    }
                    
                    players.put(name, player);
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return players;
    }
    
    /**
     * Obtiene el contenido de texto de un elemento hijo especificado.
     * 
     * @param parent El elemento padre.
     * @param tagName El nombre de la etiqueta del elemento hijo.
     * @return El contenido de texto del elemento hijo, o null si no se encuentra.
     */
    private static String getElementContent(Element parent, String tagName) {
        NodeList nodes = parent.getElementsByTagName(tagName);
        if (nodes.getLength() > 0) {
            return nodes.item(0).getTextContent();
        }
        return null;
    }
}