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
    
    // Guarda la lista de jugadores en XML
    public static void savePlayers(Map<String, Player> players) {
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
                nameElement.appendChild(doc.createTextNode(player.getName()));
                playerElement.appendChild(nameElement);
                
                Element passwordElement = doc.createElement("password");
                passwordElement.appendChild(doc.createTextNode(player.getPasswordHash()));
                playerElement.appendChild(passwordElement);
                
                Element emailElement = doc.createElement("email");
                emailElement.appendChild(doc.createTextNode(player.getEmail()));
                playerElement.appendChild(emailElement);
                
                if (player.getRecoveryToken() != null) {
                    Element tokenElement = doc.createElement("token");
                    tokenElement.appendChild(doc.createTextNode(player.getRecoveryToken()));
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
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Carga la lista de jugadores desde XML
    public static Map<String, Player> loadPlayers() {
        Map<String, Player> players = new HashMap<>();
        
        try {
            File file = new File(PLAYERS_FILE);
            if (!file.exists()) {
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
    
    private static String getElementContent(Element parent, String tagName) {
        NodeList nodes = parent.getElementsByTagName(tagName);
        if (nodes.getLength() > 0) {
            return nodes.item(0).getTextContent();
        }
        return null;
    }
}