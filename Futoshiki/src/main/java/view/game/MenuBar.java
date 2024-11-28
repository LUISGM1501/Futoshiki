package view.game;

import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MenuBar extends JMenuBar {
    private JMenu configMenu;
    private JMenu playMenu;
    private JMenu top10Menu;
    private JMenu helpMenu;
    private JMenu aboutMenu;
    private JMenuBar menuBar;

    /**
     * Constructor de la clase MenuBar.
     * Inicializa y añade los menús a la barra de menú.
     */
    public MenuBar() {
        initializeMenus();
        addMenus();
    }

    /**
     * Obtiene la barra de menú.
     * 
     * @return La barra de menú.
     */
    public JMenuBar getMenuBar() {
        return menuBar;
    }

    /**
     * Inicializa los menús de la barra de menú.
     */
    private void initializeMenus() {
        // Menú Configurar
        configMenu = new JMenu("Configurar");
        JMenuItem configItem = new JMenuItem("Opciones");
        configMenu.add(configItem);

        // Menú Jugar
        playMenu = new JMenu("Jugar");
        JMenuItem newGameItem = new JMenuItem("Nueva partida");
        playMenu.add(newGameItem);

        // Menú Top 10
        top10Menu = new JMenu("Top 10");
        JMenuItem showTop10Item = new JMenuItem("Ver Top 10");
        top10Menu.add(showTop10Item);

        // Menú Ayuda
        helpMenu = new JMenu("Ayuda");
        JMenuItem manualItem = new JMenuItem("Manual de usuario");
        helpMenu.add(manualItem);

        // Menú Acerca de
        aboutMenu = new JMenu("Acerca de");
        JMenuItem aboutItem = new JMenuItem("Información");
        aboutMenu.add(aboutItem);
    }

    /**
     * Añade los menús a la barra de menú.
     */
    private void addMenus() {
        add(configMenu);
        add(playMenu);
        add(top10Menu);
        add(helpMenu);
        add(aboutMenu);
    }

    /**
     * Añade un ActionListener al menú de configuración.
     * 
     * @param listener El ActionListener a añadir.
     */
    public void addConfigListener(ActionListener listener) {
        configMenu.getItem(0).addActionListener(listener);
    }

    /**
     * Añade un ActionListener al menú de jugar.
     * 
     * @param listener El ActionListener a añadir.
     */
    public void addPlayListener(ActionListener listener) {
        playMenu.getItem(0).addActionListener(listener);
    }

    /**
     * Añade un ActionListener al menú de Top 10.
     * 
     * @param listener El ActionListener a añadir.
     */
    public void addTop10Listener(ActionListener listener) {
        top10Menu.getItem(0).addActionListener(listener);
    }

    /**
     * Añade un ActionListener al menú de ayuda.
     * 
     * @param listener El ActionListener a añadir.
     */
    public void addHelpListener(ActionListener listener) {
        helpMenu.getItem(0).addActionListener(listener);
    }

    /**
     * Añade un ActionListener al menú de acerca de.
     * 
     * @param listener El ActionListener a añadir.
     */
    public void addAboutListener(ActionListener listener) {
        aboutMenu.getItem(0).addActionListener(listener);
    }
}
