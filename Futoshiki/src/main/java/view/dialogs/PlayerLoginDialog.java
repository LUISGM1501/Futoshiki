package view.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import model.player.PlayerManager;

/**
 * Clase PlayerLoginDialog que representa un diálogo para el inicio de sesión y registro de jugadores.
 */
public class PlayerLoginDialog extends JDialog {
    // Colores del tema
    private static final Color PRIMARY_COLOR = new Color(25, 118, 210);     // Azul
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 245); // Gris muy claro
    private static final Color TEXT_COLOR = new Color(33, 33, 33);         // Casi negro
    private static final Color ACCENT_COLOR = new Color(66, 165, 245);     // Azul claro

    private JTabbedPane tabbedPane;
    private JTextField loginNameField;
    private JPasswordField loginPasswordField;
    private JTextField registerNameField;
    private JPasswordField registerPasswordField;
    private JTextField registerEmailField;
    private JButton loginButton;
    private JButton registerButton;
    private JButton playAsGuestButton;
    private JButton forgotPasswordButton;
    private PlayerManager playerManager;
    private boolean loggedIn;
    private String playerName;

    /**
     * Constructor de la clase PlayerLoginDialog.
     * 
     * @param owner El frame propietario del diálogo.
     * @param playerManager El gestor de jugadores.
     */
    public PlayerLoginDialog(Frame owner, PlayerManager playerManager) {
        super(owner, "Futoshiki", true);
        this.playerManager = playerManager;
        this.loggedIn = false;
        
        initComponents();
        layoutComponents();
        setupListeners();
        
        setSize(350, 450);
        setLocationRelativeTo(owner);
    }

    /**
     * Inicializa los componentes del diálogo.
     */
    private void initComponents() {
        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBackground(BACKGROUND_COLOR);
        
        // Pestañas
        tabbedPane = new JTabbedPane(JTabbedPane.TOP) {
            @Override
            public void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                                  RenderingHints.VALUE_ANTIALIAS_ON);
                super.paintComponent(g);
            }
        };
        tabbedPane.setBorder(null);
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabbedPane.setForeground(TEXT_COLOR);
        
        // Crear paneles
        tabbedPane.addTab("Iniciar Sesión", createLoginPanel());
        tabbedPane.addTab("Registrarse", createRegisterPanel());
        
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
        // Panel inferior
        playAsGuestButton = createActionButton("Continuar como Invitado");
        playAsGuestButton.setForeground(Color.BLACK);
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(BACKGROUND_COLOR);
        bottomPanel.setBorder(new EmptyBorder(10, 0, 15, 0));
        bottomPanel.add(playAsGuestButton);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        setContentPane(mainPanel);
    }
    
    /**
     * Crea el panel de inicio de sesión.
     * 
     * @return El panel de inicio de sesión.
     */
    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(null); // Usando absolute positioning
        panel.setBackground(Color.WHITE);
        
        // Usuario
        JLabel userLabel = createLabel("Usuario:", 30, 30);
        loginNameField = createTextField(30, 55, 270, 35);
        
        // Contraseña
        JLabel passLabel = createLabel("Contraseña:", 30, 110);
        JPanel passwordPanel = new JPanel(new BorderLayout(5, 0));
        loginPasswordField = new JPasswordField();
        JToggleButton showPasswordButton = new JToggleButton("👁");
        showPasswordButton.setFocusPainted(false);
        
        showPasswordButton.addActionListener(e -> {
            if (showPasswordButton.isSelected()) {
                loginPasswordField.setEchoChar((char)0); // Mostrar texto
            } else {
                loginPasswordField.setEchoChar('●'); // Ocultar texto
            }
        });
        
        passwordPanel.add(loginPasswordField, BorderLayout.CENTER);
        passwordPanel.add(showPasswordButton, BorderLayout.EAST);
        passwordPanel.setBounds(30, 135, 270, 35);
        
        // Olvidé contraseña
        forgotPasswordButton = createLinkButton("¿Olvidaste tu contraseña?");
        forgotPasswordButton.setBounds(30, 180, 200, 25);
        
        // Botón login
        loginButton = createActionButton("Iniciar Sesión");
        loginButton.setBounds(30, 230, 270, 40);
        loginButton.setForeground(Color.BLACK); // Cambiar color de texto a negro
        
        panel.add(userLabel);
        panel.add(loginNameField);
        panel.add(passLabel);
        panel.add(passwordPanel);
        panel.add(forgotPasswordButton);
        panel.add(loginButton);
        
        return panel;
    }
    
    /**
     * Crea el panel de registro.
     * 
     * @return El panel de registro.
     */
    private JPanel createRegisterPanel() {
        JPanel panel = new JPanel(null); // Usando absolute positioning
        panel.setBackground(Color.WHITE);
        
        // Usuario
        JLabel userLabel = createLabel("Usuario:", 30, 30);
        registerNameField = createTextField(30, 55, 270, 35);
        
        // Contraseña
        JLabel passLabel = createLabel("Contraseña:", 30, 110);
        JPanel regPasswordPanel = new JPanel(new BorderLayout(5, 0));
        registerPasswordField = new JPasswordField();
        JToggleButton showRegPasswordButton = new JToggleButton("👁");
        showRegPasswordButton.setFocusPainted(false);
        
        showRegPasswordButton.addActionListener(e -> {
            if (showRegPasswordButton.isSelected()) {
                registerPasswordField.setEchoChar((char)0);
            } else {
                registerPasswordField.setEchoChar('●');
            }
        });
        
        regPasswordPanel.add(registerPasswordField, BorderLayout.CENTER);
        regPasswordPanel.add(showRegPasswordButton, BorderLayout.EAST);
        regPasswordPanel.setBounds(30, 135, 270, 35);
        
        // Email
        JLabel emailLabel = createLabel("Email:", 30, 190);
        registerEmailField = createTextField(30, 215, 270, 35);
        
        // Botón registro
        registerButton = createActionButton("Registrarse");
        registerButton.setBounds(30, 280, 270, 40);
        registerButton.setForeground(Color.BLACK); // Cambiar color de texto a negro
        
        panel.add(userLabel);
        panel.add(registerNameField);
        panel.add(passLabel);
        panel.add(regPasswordPanel);
        panel.add(emailLabel);
        panel.add(registerEmailField);
        panel.add(registerButton);
        
        return panel;
    }
    
    /**
     * Crea una etiqueta con el texto y posición especificados.
     * 
     * @param text El texto de la etiqueta.
     * @param x La posición x de la etiqueta.
     * @param y La posición y de la etiqueta.
     * @return La etiqueta creada.
     */
    private JLabel createLabel(String text, int x, int y) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(TEXT_COLOR);
        label.setBounds(x, y, 200, 20);
        return label;
    }
    
    /**
     * Crea un campo de texto con la posición y tamaño especificados.
     * 
     * @param x La posición x del campo de texto.
     * @param y La posición y del campo de texto.
     * @param width El ancho del campo de texto.
     * @param height La altura del campo de texto.
     * @return El campo de texto creado.
     */
    private JTextField createTextField(int x, int y, int width, int height) {
        JTextField field = new JTextField();
        field.setBounds(x, y, width, height);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setForeground(TEXT_COLOR);
        field.setBackground(Color.WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(0, 0, 2, 0, ACCENT_COLOR),
            new EmptyBorder(5, 8, 5, 8)
        ));
        return field;
    }
    
    /**
     * Crea un campo de contraseña con la posición y tamaño especificados.
     * 
     * @param x La posición x del campo de contraseña.
     * @param y La posición y del campo de contraseña.
     * @param width El ancho del campo de contraseña.
     * @param height La altura del campo de contraseña.
     * @return El campo de contraseña creado.
     */
    private JPasswordField createPasswordField(int x, int y, int width, int height) {
        JPasswordField field = new JPasswordField();
        field.setBounds(x, y, width, height);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setForeground(TEXT_COLOR);
        field.setBackground(Color.WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(0, 0, 2, 0, ACCENT_COLOR),
            new EmptyBorder(5, 8, 5, 8)
        ));
        field.setEchoChar('●');
        return field;
    }
    
    /**
     * Crea un botón de acción con el texto especificado.
     * 
     * @param text El texto del botón.
     * @return El botón de acción creado.
     */
    private JButton createActionButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setBackground(PRIMARY_COLOR);
        button.setBorder(new EmptyBorder(8, 15, 8, 15));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Efecto hover
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(ACCENT_COLOR);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(PRIMARY_COLOR);
            }
        });
        
        return button;
    }
    
    /**
     * Crea un botón de enlace con el texto especificado.
     * 
     * @param text El texto del botón.
     * @return El botón de enlace creado.
     */
    private JButton createLinkButton(String text) {
        JButton button = new JButton(text);
        button.setBorder(null);
        button.setContentAreaFilled(false);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        button.setForeground(ACCENT_COLOR);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Efecto hover
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setForeground(PRIMARY_COLOR);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setForeground(ACCENT_COLOR);
            }
        });
        
        return button;
    }
    
    /**
     * Organiza los componentes del diálogo.
     */
    private void layoutComponents() {
        setLayout(new BorderLayout());
        add(tabbedPane, BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(playAsGuestButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Configura los listeners para los botones.
     */
    private void setupListeners() {
        loginButton.addActionListener(e -> handleLogin());
        registerButton.addActionListener(e -> handleRegister());
        playAsGuestButton.addActionListener(e -> handleGuest());
        forgotPasswordButton.addActionListener(e -> handleForgotPassword());
    }
    
    /**
     * Maneja el evento de inicio de sesión.
     */
    private void handleLogin() {
        String name = loginNameField.getText();
        String password = new String(loginPasswordField.getPassword());

        System.out.println("Intentando login con usuario: " + name + " y contraseña: " + password);
        
        String error = playerManager.login(name, password);
        if (error == null) {
            loggedIn = true;
            playerName = name;
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Maneja el evento de registro.
     */
    private void handleRegister() {
        String name = registerNameField.getText();
        String password = new String(registerPasswordField.getPassword());
        String email = registerEmailField.getText();
        
        String error = playerManager.registerPlayer(name, password, email);
        if (error == null) {
            JOptionPane.showMessageDialog(this, 
                "Registro exitoso. Ahora puedes iniciar sesión.", 
                "Registro", JOptionPane.INFORMATION_MESSAGE);
            tabbedPane.setSelectedIndex(0);
            loginNameField.setText(name);
        } else {
            JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Maneja el evento de jugar como invitado.
     */
    private void handleGuest() {
        loggedIn = false;
        playerName = "Invitado"; // Cambiado de cadena vacía a "Invitado"
        dispose();
    }
    
    /**
     * Maneja el evento de olvido de contraseña.
     */
    private void handleForgotPassword() {
        String name = JOptionPane.showInputDialog(this, 
            "Ingresa tu nombre de usuario para recuperar la contraseña:");
            
        if (name != null && !name.trim().isEmpty()) {
            String result = playerManager.initiatePasswordRecovery(name);
            // Mostrar el token directamente
            JOptionPane.showMessageDialog(this,
                "Tu token de recuperación es: " + result + "\n" +
                "Por favor, usa este token para restablecer tu contraseña.",
                "Token de Recuperación",
                JOptionPane.INFORMATION_MESSAGE);

            System.out.println("Token de recuperación: " + result);
            // Después de mostrar el token, abrir diálogo para resetear contraseña
            showPasswordResetDialog(name);
        }
    }

    /**
     * Muestra el diálogo para restablecer la contraseña.
     * 
     * @param username El nombre de usuario.
     */
    private void showPasswordResetDialog(String username) {
        JPanel panel = new JPanel();
        BoxLayout boxLayout = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(boxLayout);
        
        JTextField tokenField = new JTextField(20);
        JPasswordField newPasswordField = new JPasswordField(20);
        
        panel.add(new JLabel("Token de recuperación:"));
        panel.add(tokenField);
        panel.add(new JLabel("Nueva contraseña:"));
        panel.add(newPasswordField);
        
        int result = JOptionPane.showConfirmDialog(this, panel, 
            "Restablecer Contraseña", 
            JOptionPane.OK_CANCEL_OPTION);
            
        if (result == JOptionPane.OK_OPTION) {
            String token = tokenField.getText();
            String newPassword = new String(newPasswordField.getPassword());
            
            String error = playerManager.completePasswordRecovery(username, token, newPassword);
            if (error == null) {
                JOptionPane.showMessageDialog(this,
                    "Contraseña actualizada exitosamente",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                    error,
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Verifica si el usuario ha iniciado sesión.
     * 
     * @return true si el usuario ha iniciado sesión, false en caso contrario.
     */
    public boolean isLoggedIn() {
        return loggedIn;
    }
    
    /**
     * Obtiene el nombre del jugador.
     * Si no hay nombre establecido, devuelve "Invitado".
     * 
     * @return El nombre del jugador.
     */
    public String getPlayerName() {
        return playerName != null ? playerName : "Invitado";
    }
}