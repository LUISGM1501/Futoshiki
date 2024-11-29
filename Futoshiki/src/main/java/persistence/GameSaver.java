package persistence;

import java.io.File;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.w3c.dom.*;
import java.util.ArrayList;
import java.util.List;
import model.game.*;
import model.config.Configuration;
import util.constants.FileConstants;

import controller.timer.TimerController;

public class GameSaver {
    
    /**
     * Guarda el estado actual del juego.
     * 
     * @param gameState El estado del juego a guardar.
     * @param playerName El nombre del jugador.
     * @param config La configuración del juego.
     * @param timerController El controlador del temporizador.
     * @return true si se guardó correctamente, false en caso contrario.
     */
    public static boolean saveGame(GameState gameState, String playerName, 
                                 Configuration config, TimerController timerController) {
        try {
            // Cargar documento existente o crear uno nuevo
            Document doc;
            Element rootElement;
            File file = new File(FileConstants.CURRENT_GAME_FILE);
            
            if (file.exists()) {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                doc = dBuilder.parse(file);
                rootElement = doc.getDocumentElement();
                
                // Buscar y eliminar juego anterior del mismo jugador si existe
                NodeList savedGames = doc.getElementsByTagName("game");
                for (int i = 0; i < savedGames.getLength(); i++) {
                    Element gameElement = (Element) savedGames.item(i);
                    String savedPlayer = gameElement.getElementsByTagName("player").item(0).getTextContent();
                    if (savedPlayer.equals(playerName)) {
                        rootElement.removeChild(gameElement);
                        break;
                    }
                }
            } else {
                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
                doc = docBuilder.newDocument();
                rootElement = doc.createElement("savedGames");
                doc.appendChild(rootElement);
            }
            
            // Crear nuevo elemento de juego
            Element gameElement = doc.createElement("game");
            
            // Información del jugador
            Element playerElement = doc.createElement("player");
            playerElement.setTextContent(playerName);
            gameElement.appendChild(playerElement);
            
            // Timer info
            Element timerElement = doc.createElement("timer");
            timerElement.setAttribute("type", timerController.getCronometro());
            timerElement.setAttribute("hours", String.valueOf(timerController.getHoursPassed()));
            timerElement.setAttribute("minutes", String.valueOf(timerController.getMinutesPassed()));
            timerElement.setAttribute("seconds", String.valueOf(timerController.getSecondsPassed()));
            timerElement.setAttribute("expired", String.valueOf(timerController.isTimerExpired()));
            gameElement.appendChild(timerElement);
            
            // Configuración
            Element configElement = doc.createElement("configuration");
            configElement.setAttribute("gridSize", String.valueOf(config.getGridSize()));
            configElement.setAttribute("difficulty", config.getDifficulty());
            configElement.setAttribute("multiLevel", String.valueOf(config.isMultiLevel()));
            configElement.setAttribute("panelPosition", config.getDigitPanelPosition());
            gameElement.appendChild(configElement);
            
            // Estado del juego
            Element stateElement = doc.createElement("gameState");
            Element difficultyElement = doc.createElement("difficulty");
            difficultyElement.setTextContent(gameState.getDifficulty());
            stateElement.appendChild(difficultyElement);
            
            // Tablero
            Element boardElement = doc.createElement("board");
            FutoshikiBoard board = gameState.getBoard();
            boardElement.setAttribute("size", String.valueOf(board.getSize()));
            
            for (int i = 0; i < board.getSize(); i++) {
                for (int j = 0; j < board.getSize(); j++) {
                    Element cellElement = doc.createElement("cell");
                    cellElement.setAttribute("row", String.valueOf(i));
                    cellElement.setAttribute("col", String.valueOf(j));
                    cellElement.setAttribute("value", String.valueOf(board.getValue(i, j)));
                    cellElement.setAttribute("constant", String.valueOf(board.isConstant(i, j)));
                    
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
            
            stateElement.appendChild(boardElement);
            gameElement.appendChild(stateElement);
            
            // Agregar el nuevo juego al documento
            rootElement.appendChild(gameElement);
            
            // Guardar el documento
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.STANDALONE, "no");
            
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(file);
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
            
            // Buscar el juego del jugador específico
            NodeList gamesList = doc.getElementsByTagName("game");
            for (int i = 0; i < gamesList.getLength(); i++) {
                Element gameElement = (Element) gamesList.item(i);
                String savedPlayer = gameElement.getElementsByTagName("player").item(0).getTextContent();
                
                if (savedPlayer.equals(playerName)) {
                    // Cargar el estado del juego
                    Element stateElement = (Element) gameElement.getElementsByTagName("gameState").item(0);
                    String difficulty = stateElement.getElementsByTagName("difficulty").item(0).getTextContent();
                    
                    Element boardElement = (Element) stateElement.getElementsByTagName("board").item(0);
                    int size = Integer.parseInt(boardElement.getAttribute("size"));
                    FutoshikiBoard board = new FutoshikiBoard(size);
                    
                    NodeList cells = boardElement.getElementsByTagName("cell");
                    for (int j = 0; j < cells.getLength(); j++) {
                        Element cell = (Element) cells.item(j);
                        int row = Integer.parseInt(cell.getAttribute("row"));
                        int col = Integer.parseInt(cell.getAttribute("col"));
                        int value = Integer.parseInt(cell.getAttribute("value"));
                        boolean constant = Boolean.parseBoolean(cell.getAttribute("constant"));
                        
                        if (constant) {
                            board.setConstant(row, col, value);
                        } else if (value > 0) {
                            board.setCellValue(row, col, value);
                        }
                        
                        if (cell.hasAttribute("rightInequality")) {
                            board.setInequality("maf", row, col);
                        }
                        if (cell.hasAttribute("bottomInequality")) {
                            board.setInequality("mac", row, col);
                        }
                    }
                    
                    return new GameState(board, difficulty, "");
                }
            }
            
            return null;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Verifica si hay un juego guardado para el jugador.
     * 
     * @param playerName El nombre del jugador.
     * @return true si hay un juego guardado, false en caso contrario.
     */
    public static boolean hasGameSaved(String playerName) {
        try {
            File file = new File(FileConstants.CURRENT_GAME_FILE);
            if (!file.exists()) {
                return false;
            }
            
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            
            NodeList games = doc.getElementsByTagName("game");
            for (int i = 0; i < games.getLength(); i++) {
                Element gameElement = (Element) games.item(i);
                String savedPlayer = gameElement.getElementsByTagName("player").item(0).getTextContent();
                if (savedPlayer.equals(playerName)) {
                    return true;
                }
            }
            
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}