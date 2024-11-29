package persistence;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import controller.game.RandomGames;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import model.game.Inequality;
import util.constants.FileConstants;

public class XMLHandler {
    
    /**
     * Carga las partidas desde el archivo XML.
     * 
     * @return Un mapa que asocia el nivel de dificultad con una lista de datos de juego.
     */
    public static Map<String, List<GameData>> loadGames() {
        Map<String, List<GameData>> games = new HashMap<>();
        games.put("Facil", new ArrayList<>());
        games.put("Intermedio", new ArrayList<>());
        games.put("Dificil", new ArrayList<>());
        
        try {
            File xmlFile = new File(FileConstants.GAMES_FILE);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            
            NodeList partidas = doc.getElementsByTagName("partida");
            
            for (int i = 0; i < partidas.getLength(); i++) {
                Node partidaNode = partidas.item(i);
                
                if (partidaNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element partida = (Element) partidaNode;
                    GameData gameData = parseGameData(partida);
                    games.get(gameData.getNivel()).add(gameData);
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return games;
    }

    /**
     * Parsea los datos de una partida desde un elemento XML.
     * 
     * @param partida El elemento XML que representa una partida.
     * @return Un objeto GameData con los datos de la partida.
     */

    private static GameData parseGameData(Element partida) {
        String nivel = getElementContent(partida, "nivel");
        int tamano = Integer.parseInt(getElementContent(partida, "cuadricula"));
        
        // Parsear desigualdades
        List<Inequality> desigualdades = new ArrayList<>();
        NodeList desigNodes = partida.getElementsByTagName("desigualdades");
        for (int i = 0; i < desigNodes.getLength(); i++) {
            String[] parts = desigNodes.item(i).getTextContent().split(",");
            desigualdades.add(new Inequality(
                parts[0],  // tipo
                Integer.parseInt(parts[1].trim()),  // fila
                Integer.parseInt(parts[2].trim())   // columna
            ));
        }
        
        // Parsear constantes
        Map<String, Integer> constantes = new HashMap<>();
        NodeList constNodes = partida.getElementsByTagName("constantes");
        for (int i = 0; i < constNodes.getLength(); i++) {
            String[] parts = constNodes.item(i).getTextContent().split(",");
            String key = parts[1].trim() + "," + parts[2].trim(); // fila,columna
            constantes.put(key, Integer.parseInt(parts[0].trim()));
        }
        
        return new GameData(nivel, tamano, desigualdades, constantes);
    }
    
    /**
     * Obtiene el contenido de texto de un elemento hijo especificado.
     * 
     * @param parent El elemento padre.
     * @param tagName El nombre de la etiqueta del elemento hijo.
     * @return El contenido de texto del elemento hijo, o una cadena vacía si no se encuentra.
     */
    private static String getElementContent(Element parent, String tagName) {
        NodeList nodes = parent.getElementsByTagName(tagName);
        if (nodes.getLength() > 0) {
            return nodes.item(0).getTextContent();
        }
        return "";
    }
    
    // Clase interna para almacenar los datos de una partida
    public static class GameData {
        private String nivel;
        private int tamano;
        private List<Inequality> desigualdades;
        private Map<String, Integer> constantes;
        
        /**
         * Constructor para crear una instancia de GameData.
         * 
         * @param nivel El nivel de dificultad del juego.
         * @param tamano El tamaño de la cuadrícula del juego.
         * @param desigualdades La lista de desigualdades en el juego.
         * @param constantes Un mapa de constantes en el juego, indexado por "fila,columna".
         */
        public GameData(String nivel, int tamano, List<Inequality> desigualdades, 
                       Map<String, Integer> constantes) {
            this.nivel = nivel;
            this.tamano = tamano;
            this.desigualdades = desigualdades;
            this.constantes = constantes;
        }
        
        // Getters
        public String getNivel() { return nivel; }
        public int getTamano() { return tamano; }
        public List<Inequality> getDesigualdades() { return desigualdades; }
        public Map<String, Integer> getConstantes() { return constantes; }
    }
}