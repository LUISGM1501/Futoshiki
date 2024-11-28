package persistence;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import model.config.Configuration;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import model.game.FutoshikiBoard;
import model.game.GameState;
import util.constants.FileConstants;


public class GameSaver {
    
    /**
     * Guarda el estado actual del juego.
     * 
     * @param gameState El estado del juego a guardar.
     * @param playerName El nombre del jugador.
     * @param config La configuración del juego.
     * @return true si se guardó correctamente, false en caso contrario.
     */
    public static boolean saveGame(GameState gameState, String playerName, Configuration config) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            
            // Elemento raíz
            Element rootElement = doc.createElement("savedGame");
            doc.appendChild(rootElement);
            
            // Información del jugador
            Element playerElement = doc.createElement("player");
            playerElement.setTextContent(playerName);
            rootElement.appendChild(playerElement);
            
            // Información del juego
            Element gameElement = doc.createElement("gameState");
            // Dificultad
            Element difficultyElement = doc.createElement("difficulty");
            difficultyElement.setTextContent(gameState.getDifficulty());
            gameElement.appendChild(difficultyElement);
            //Config
            Element configElement = doc.createElement("config");
            configElement.setTextContent(config.toString());
            gameElement.appendChild(configElement);
            
            // Tablero
            Element boardElement = doc.createElement("board");
            FutoshikiBoard board = gameState.getBoard();
            boardElement.setAttribute("size", String.valueOf(board.getSize()));

            // Guardar estado de cada celda
            for (int i = 0; i < board.getSize(); i++) {
                for (int j = 0; j < board.getSize(); j++) {
                    Element cellElement = doc.createElement("cell");
                    cellElement.setAttribute("row", String.valueOf(i));
                    cellElement.setAttribute("col", String.valueOf(j));
                    cellElement.setAttribute("value", String.valueOf(board.getValue(i, j)));
                    cellElement.setAttribute("constant", String.valueOf(board.isConstant(i, j)));
                    
                    // Desigualdades
                    String rightIneq = board.getRightInequality(i, j);
                    String bottomIneq = board.getBottomInequality(i, j);
                    
                    if (!rightIneq.equals(" ")) {
                        cellElement.setAttribute("rightInequality", rightIneq);
                    }
                    if (!bottomIneq.equals(" ")) {
                        cellElement.setAttribute("bottomInequality", bottomIneq);
                    }
                    
                    boardElement.appendChild(cellElement);
                }
            }
            
            gameElement.appendChild(boardElement);
            rootElement.appendChild(gameElement);
            
            // Guardar el documento XML
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(FileConstants.CURRENT_GAME_FILE));
            transformer.transform(source, result);
            
            return true;
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Carga el último juego guardado.
     * 
     * @param playerName El nombre del jugador.
     * @return GameState con el estado del juego o null si no hay juego guardado.
     */
    public static GameState loadGame(String playerName) {
        try {
            File file = new File(FileConstants.CURRENT_GAME_FILE);
            if (!file.exists()) {
                return null;
            }
            
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();
            
            // Verificar que el juego pertenece al jugador
            NodeList playerNodes = doc.getElementsByTagName("player");
            /*
            if (playerNodes.getLength() > 0) {
                String savedPlayer = playerNodes.item(0).getTextContent();
                if (!savedPlayer.equals(playerName)) {
                    return null;
                }
            }
            */
            // Cargar estado del juego
            Element gameElement = (Element) doc.getElementsByTagName("gameState").item(0);
            String difficulty = gameElement.getElementsByTagName("difficulty").item(0).getTextContent();
            String config = gameElement.getElementsByTagName("config").item(0).getTextContent();



            // Cargar tablero
            Element boardElement = (Element) gameElement.getElementsByTagName("board").item(0);
            int size = Integer.parseInt(boardElement.getAttribute("size"));
            FutoshikiBoard board = new FutoshikiBoard(size);
            
            // Cargar celdas
            NodeList cells = boardElement.getElementsByTagName("cell");
            for (int i = 0; i < cells.getLength(); i++) {
                Element cell = (Element) cells.item(i);
                int row = Integer.parseInt(cell.getAttribute("row"));
                int col = Integer.parseInt(cell.getAttribute("col"));
                int value = Integer.parseInt(cell.getAttribute("value"));
                boolean constant = Boolean.parseBoolean(cell.getAttribute("constant"));
                
                if (constant) {
                    board.setConstant(row, col, value);
                } else if (value > 0) {
                    board.setCellValue(row, col, value);
                }
                
                // Cargar desigualdades
                if (cell.hasAttribute("rightInequality")) {
                    board.setInequality("maf", row, col);
                }
                if (cell.hasAttribute("bottomInequality")) {
                    board.setInequality("mac", row, col);
                }
            }
            
            return new GameState(board, difficulty, config);
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}