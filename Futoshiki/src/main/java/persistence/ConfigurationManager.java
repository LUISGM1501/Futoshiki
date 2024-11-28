package persistence;

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
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import model.config.Configuration;
import util.constants.FileConstants;

public class ConfigurationManager {

    /**
     * Guarda la configuración en un archivo XML.
     * 
     * @param config La configuración a guardar.
     * @return true si la configuración se guardó correctamente, false en caso contrario.
     */
    public static boolean saveConfiguration(Configuration config) {
        try {
            System.out.println("Intentando guardar configuración...");
            
            // Crear el directorio data si no existe
            Path dataDir = Paths.get("data");
            if (!Files.exists(dataDir)) {
                Files.createDirectories(dataDir);
            }
            
            File configFile = new File(FileConstants.CONFIG_FILE);
            System.out.println("Guardando configuración en: " + configFile.getAbsolutePath());

            // Crear el documento XML
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();
            doc.setXmlStandalone(true); // Importante para el formato correcto

            // Crear elemento raíz
            Element rootElement = doc.createElement("configuracion");
            doc.appendChild(rootElement);

            // Imprimir valores que se van a guardar
            System.out.println("Guardando valores:");
            System.out.println("- Tamaño: " + config.getGridSize());
            System.out.println("- Dificultad: " + config.getDifficulty());
            System.out.println("- Multinivel: " + config.isMultiLevel());
            System.out.println("- Tipo Timer: " + config.getTimerType());
            System.out.println("- Posición Panel: " + config.getDigitPanelPosition());
            System.out.println("- Jugador: " + config.getPlayerName());

            // Agregar elementos de configuración
            addConfigElement(doc, rootElement, "tamano", String.valueOf(config.getGridSize()));
            addConfigElement(doc, rootElement, "dificultad", config.getDifficulty());
            addConfigElement(doc, rootElement, "multinivel", String.valueOf(config.isMultiLevel()));
            addConfigElement(doc, rootElement, "tipoTimer", config.getTimerType());
            addConfigElement(doc, rootElement, "horasTimer", String.valueOf(config.getTimerHours()));
            addConfigElement(doc, rootElement, "minutosTimer", String.valueOf(config.getTimerMinutes()));
            addConfigElement(doc, rootElement, "segundosTimer", String.valueOf(config.getTimerSeconds()));
            addConfigElement(doc, rootElement, "posicionPanel", config.getDigitPanelPosition());
            addConfigElement(doc, rootElement, "jugador", config.getPlayerName());

            // Configurar el transformer para escribir el XML con formato
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");

            // Escribir el documento al archivo
            try (FileOutputStream fos = new FileOutputStream(configFile)) {
                StreamResult result = new StreamResult(fos);
                transformer.transform(new DOMSource(doc), result);
            }

            // Verificar que el archivo se guardó correctamente
            if (configFile.exists() && configFile.length() > 0) {
                System.out.println("Configuración guardada exitosamente");
                System.out.println("Tamaño del archivo: " + configFile.length() + " bytes");
                // Imprimir el contenido del archivo para debug
                System.out.println("Contenido del archivo:");
                System.out.println(new String(Files.readAllBytes(configFile.toPath())));
                return true;
            } else {
                System.err.println("Error: El archivo está vacío o no se creó correctamente");
                return false;
            }
        } catch (Exception e) {
            System.err.println("Error al guardar la configuración: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Agrega un elemento de configuración al documento XML.
     * 
     * @param doc El documento XML.
     * @param parent El elemento padre al que se agregará el nuevo elemento.
     * @param name El nombre del nuevo elemento.
     * @param value El valor del nuevo elemento.
     */
    private static void addConfigElement(Document doc, Element parent, String name, String value) {
        Element element = doc.createElement(name);
        // Asegurarse de que nunca se guarde un valor null
        element.setTextContent(value != null ? value : "");
        parent.appendChild(element);
    }

    /**
     * Carga la configuración desde un archivo XML.
     * 
     * @return La configuración cargada.
     */
    public static Configuration loadConfiguration() {
        try {
            File file = new File(FileConstants.CONFIG_FILE);
            System.out.println("Intentando cargar configuración desde: " + file.getAbsolutePath());
            
            if (!file.exists() || file.length() == 0) {
                System.out.println("Archivo de configuración no encontrado o vacío, creando configuración por defecto");
                Configuration defaultConfig = new Configuration();
                saveConfiguration(defaultConfig);
                return defaultConfig;
            }

            // Imprimir contenido del archivo para debug
            System.out.println("Contenido del archivo de configuración:");
            System.out.println(new String(Files.readAllBytes(file.toPath())));

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();

            Configuration config = new Configuration();
            NodeList nodes = doc.getElementsByTagName("*");

            for (int i = 0; i < nodes.getLength(); i++) {
                if (nodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) nodes.item(i);
                    String nodeName = element.getNodeName();
                    String value = element.getTextContent();

                    // Debug: imprimir cada nodo y su valor
                    System.out.println("Nodo: " + nodeName + ", Valor: " + value);

                    switch (nodeName) {
                        case "tamano":
                            config.setGridSize(Integer.parseInt(value));
                            break;
                        case "dificultad":
                            config.setDifficulty(value);
                            break;
                        case "multinivel":
                            config.setMultiLevel(Boolean.parseBoolean(value));
                            break;
                        case "tipoTimer":
                            config.setTimerType(value);
                            break;
                        case "horasTimer":
                            config.setTimerHours(Integer.parseInt(value));
                            break;
                        case "minutosTimer":
                            config.setTimerMinutes(Integer.parseInt(value));
                            break;
                        case "segundosTimer":
                            config.setTimerSeconds(Integer.parseInt(value));
                            break;
                        case "posicionPanel":
                            config.setDigitPanelPosition(value);
                            break;
                        case "jugador":
                            config.setPlayerName(value);
                            break;
                    }
                }
            }

            return config;
        } catch (Exception e) {
            System.err.println("Error al cargar la configuración: " + e.getMessage());
            e.printStackTrace();
            return new Configuration();
        }
    }
}
