package persistence;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import model.game.Inequality;
import util.constants.FileConstants;

public class XMLHandler {
    
    // Carga las partidas desde el archivo XML
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