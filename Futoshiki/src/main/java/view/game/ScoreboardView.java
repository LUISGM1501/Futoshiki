package view.game;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import persistence.Top10Manager;
import java.util.List;
import model.game.GameScore;
import javax.swing.JLabel;
import java.awt.FlowLayout;

/**
 * Clase ScoreboardView que representa la vista del tablero de puntuaciones.
 */
public class ScoreboardView extends JPanel {
    private JTable scoreTable;
    private JComboBox<String> difficultyFilter;
    private JComboBox<String> sizeFilter; // Nuevo filtro para tamaño
    private DefaultTableModel tableModel;
    private Top10Manager top10Manager;
    private static ScoreboardView instance;

    /**
     * Constructor de ScoreboardView.
     * Inicializa los componentes y carga los scores iniciales.
     */
    public ScoreboardView() {
        this.top10Manager = Top10Manager.getInstance();
        setLayout(new BorderLayout(10, 10));
        
        initializeComponents();
        layoutComponents();
        updateScores(); // Cargar scores iniciales
    }

    /**
     * Obtiene la instancia única de ScoreboardView.
     * 
     * @return La instancia de ScoreboardView.
     */
    public static ScoreboardView getInstance() {
        if (instance == null) {
            instance = new ScoreboardView();
        }
        return instance;
    }
    
    /**
     * Inicializa los componentes de la vista.
     */
    private void initializeComponents() {
        // Crear modelo de tabla
        String[] columnNames = {"Posición", "Jugador", "Tiempo", "Tamaño"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer la tabla no editable
            }
        };
        scoreTable = new JTable(tableModel);
        
        // Configurar tabla
        scoreTable.getColumnModel().getColumn(0).setPreferredWidth(60);  // Posición
        scoreTable.getColumnModel().getColumn(1).setPreferredWidth(150); // Jugador
        scoreTable.getColumnModel().getColumn(2).setPreferredWidth(100); // Tiempo
        scoreTable.getColumnModel().getColumn(3).setPreferredWidth(60);  // Tamaño
        
        // Filtro de dificultad
        difficultyFilter = new JComboBox<>(new String[]{"Fácil", "Intermedio", "Difícil"});
        difficultyFilter.addActionListener(e -> updateScores());

        // Crear filtro de tamaño
        String[] sizes = new String[]{"3x3", "4x4", "5x5", "6x6", "7x7", "8x8", "9x9", "10x10"};
        sizeFilter = new JComboBox<>(sizes);
        sizeFilter.addActionListener(e -> updateScores());

        // Panel de filtros
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        filterPanel.add(new JLabel("Dificultad: "));
        filterPanel.add(difficultyFilter);
        filterPanel.add(new JLabel("Tamaño: "));
        filterPanel.add(sizeFilter);
        
        // Agregar panel de filtros al panel principal
        add(filterPanel, BorderLayout.NORTH);
    }
    
    /**
     * Organiza los componentes en el panel.
     */
    private void layoutComponents() {
        // Agregar componentes al panel principal
        add(new JScrollPane(scoreTable), BorderLayout.CENTER);
        
        // Tamaño inicial
        setPreferredSize(new Dimension(400, 300));
    }
    
    /**
     * Actualiza los scores mostrados en la tabla.
     */
    public void updateScores() {
        // Limpiar tabla actual
        tableModel.setRowCount(0);
        
        // Obtener dificultad seleccionada
        String selectedDifficulty = mapDifficultyToInternal((String)difficultyFilter.getSelectedItem());
        int selectedSize = Integer.parseInt(((String)sizeFilter.getSelectedItem()).split("x")[0]);
        
        // Obtener scores para la dificultad y tamaño seleccionados
        List<GameScore> scores = top10Manager.getScoresByLevelAndSize(selectedDifficulty, selectedSize);
        
        // Agregar scores a la tabla
        for (int i = 0; i < scores.size(); i++) {
            GameScore score = scores.get(i);
            tableModel.addRow(new Object[]{
                i + 1, // Posición
                score.getPlayerName(),
                formatTime(score.getHours(), score.getMinutes(), score.getSeconds()),
                score.getGridSize() + "x" + score.getGridSize()
            });
        }
    }
    
    /**
     * Formatea el tiempo en horas, minutos y segundos.
     * 
     * @param hours Horas.
     * @param minutes Minutos.
     * @param seconds Segundos.
     * @return El tiempo formateado como cadena.
     */
    private String formatTime(int hours, int minutes, int seconds) {
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
    
    /**
     * Mapea la dificultad mostrada a su representación interna.
     * 
     * @param displayDifficulty La dificultad mostrada.
     * @return La dificultad interna.
     */
    private String mapDifficultyToInternal(String displayDifficulty) {
        switch(displayDifficulty) {
            case "Fácil": return "Facil";
            case "Intermedio": return "Intermedio";
            case "Difícil": return "Dificil";
            default: return displayDifficulty;
        }
    }
}