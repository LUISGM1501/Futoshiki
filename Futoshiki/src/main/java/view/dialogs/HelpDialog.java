package view.dialogs;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * Clase HelpDialog que representa un diálogo de ayuda con imágenes de un manual de usuario.
 */
public class HelpDialog extends JDialog {
    private static final String[] HELP_IMAGES = {
        "manual2.png","manual3.png", "manual4.png","manual5.png"
    };
    
    private static final String IMAGE_PATH = "src/main/java/view/dialogs/Imagenes/";
    private int currentImageIndex = 0;
    private JLabel imageLabel;
    
    /**
     * Constructor de HelpDialog.
     * 
     * @param owner El frame propietario del diálogo.
     */
    public HelpDialog(Frame owner) {
        super(owner, "Manual de Usuario", true);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Panel para la imagen
        imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(800, 600));
        mainPanel.add(new JScrollPane(imageLabel), BorderLayout.CENTER);
        
        // Panel de navegación
        JPanel navigationPanel = new JPanel(new FlowLayout());
        JButton prevButton = new JButton("Anterior");
        JButton nextButton = new JButton("Siguiente");
        
        prevButton.addActionListener(e -> showPreviousImage());
        nextButton.addActionListener(e -> showNextImage());
        
        navigationPanel.add(prevButton);
        navigationPanel.add(nextButton);
        mainPanel.add(navigationPanel, BorderLayout.SOUTH);
        
        setContentPane(mainPanel);
        showImage(0);
        
        pack();
        setLocationRelativeTo(owner);
    }
    
    /**
     * Muestra la imagen en el índice especificado.
     * 
     * @param index El índice de la imagen a mostrar.
     */
    private void showImage(int index) {
        try {
            File imageFile = new File(IMAGE_PATH + HELP_IMAGES[index]);
            System.out.println("Intentando cargar imagen desde: " + imageFile.getAbsolutePath());
            
            if (!imageFile.exists()) {
                throw new Exception("La imagen no existe: " + imageFile.getAbsolutePath());
            }
            
            ImageIcon icon = new ImageIcon(imageFile.getAbsolutePath());
            if (icon.getImageLoadStatus() != MediaTracker.COMPLETE) {
                throw new Exception("No se pudo cargar la imagen completamente");
            }
            
            Image scaledImage = icon.getImage().getScaledInstance(800, 600, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaledImage));
            currentImageIndex = index;
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Error al cargar la imagen: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Muestra la siguiente imagen en la secuencia.
     */
    private void showNextImage() {
        if (currentImageIndex < HELP_IMAGES.length - 1) {
            showImage(currentImageIndex + 1);
        }
    }
    
    /**
     * Muestra la imagen anterior en la secuencia.
     */
    private void showPreviousImage() {
        if (currentImageIndex > 0) {
            showImage(currentImageIndex - 1);
        }
    }
}