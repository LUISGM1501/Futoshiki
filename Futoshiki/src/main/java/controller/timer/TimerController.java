package controller.timer;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import model.config.Configuration;
import view.game.MainWindow;
import controller.game.GameController;

/**
 * Controlador para gestionar el temporizador del juego.
 */
public class TimerController {
    private int secondsPassed;
    private int minutesPassed;
    private int hoursPassed;
    private String cronometro;
    private boolean timerExpired;
    private int originalHours;
    private int originalMinutes;
    private int originalSeconds;
    private MainWindow mainWindow;
    private GameController gameController;

    /**
     * Constructor del TimerController.
     * 
     * @param mainWindow La ventana principal del juego.
     * @param gameController El controlador del juego.
     */
    public TimerController(MainWindow mainWindow, GameController gameController) {
        this.timerExpired = false;
        this.mainWindow = mainWindow;
        this.gameController = gameController;
    }

    /**
     * Configura los valores del temporizador a partir de una configuración dada.
     * 
     * @param config La configuración que contiene los valores del temporizador.
     */
    public void setValores(Configuration config) {
        secondsPassed = config.getTimerSeconds();
        minutesPassed = config.getTimerMinutes();
        hoursPassed = config.getTimerHours();
        cronometro = config.getTimerType();
        
        // Guardar valores originales
        originalHours = hoursPassed;
        originalMinutes = minutesPassed;
        originalSeconds = secondsPassed;
        
        // Resetear estado
        timerExpired = false;
    }

    /**
     * Actualiza el tiempo transcurrido basado en el tipo de cronómetro.
     */
    public void actualizarTiempo() {
        switch (cronometro) {
            case "Cronómetro":
                incrementTime();
                break;
                
            case "Temporizador":
                if (secondsPassed == 0 && minutesPassed == 0 && hoursPassed == 0) {
                    if (!timerExpired) {
                        timerExpired = true;
                        handleTimerExpired();
                    }
                } else {
                    decrementTime();
                }
                break;

            case "TERMINADO":
                break;
        }
    }

    /**
     * Incrementa el tiempo transcurrido.
     */
    private void incrementTime() {
        secondsPassed++;
        if (secondsPassed >= 60) {
            secondsPassed = 0;
            minutesPassed += 1;
        }
        if (minutesPassed >= 60) {
            hoursPassed += 1;
            minutesPassed = 0;
        }
    }

    /**
     * Decrementa el tiempo restante.
     */
    private void decrementTime() {
        secondsPassed--;
        if (secondsPassed < 0) {
            secondsPassed = 59;
            minutesPassed--;
            if (minutesPassed < 0) {
                minutesPassed = 59;
                hoursPassed--;
            }
        }
    }

    /**
     * Maneja el evento cuando el temporizador expira.
     */
    private void handleTimerExpired() {
        SwingUtilities.invokeLater(() -> {
            int option = JOptionPane.showConfirmDialog(mainWindow,
                "TIEMPO EXPIRADO. ¿DESEA CONTINUAR EL MISMO JUEGO?",
                "Tiempo Expirado",
                JOptionPane.YES_NO_OPTION);

            if (option == JOptionPane.YES_OPTION) {
                // Convertir a cronómetro
                cronometro = "Cronómetro";
                hoursPassed = originalHours;
                minutesPassed = originalMinutes;
                secondsPassed = originalSeconds;
            } else {
                // Forzar fin del juego
                cronometro = "TERMINADO";
                forceEndGame();
            }
        });
    }

    /**
     * Fuerza el fin del juego cuando el tiempo expira.
     */
    private void forceEndGame() {
        SwingUtilities.invokeLater(() -> {
            // Detener el timer y limpiar el tablero
            mainWindow.stopTimer();
            mainWindow.getGameBoard().reset();
            mainWindow.getGameBoard().setPlayable(false);
            mainWindow.enableGameButtons(false);

            // Mostrar mensaje
            JOptionPane.showMessageDialog(mainWindow,
                "El juego ha terminado debido a que el tiempo expiró.",
                "Juego Terminado",
                JOptionPane.INFORMATION_MESSAGE);

            // Forzar inicio de nuevo juego
            gameController.startGame();
        });
    }

    /**
     * Obtiene el tiempo transcurrido en formato HH:MM:SS.
     * 
     * @return El tiempo transcurrido en formato HH:MM:SS.
     */
    public String getTiempoTranscurrido() {
        return String.format("%02d:%02d:%02d", hoursPassed, minutesPassed, secondsPassed);
    }

    /**
     * Establece el tiempo transcurrido.
     * 
     * @param secondsPassed El tiempo transcurrido en segundos.
     */
    public void setSecondsPassed(int secondsPassed) {
        this.secondsPassed = secondsPassed;
    }

    /**
     * Establece los minutos transcurridos.
     * 
     * @param minutesPassed Los minutos transcurridos.
     */
    public void setMinutesPassed(int minutesPassed) {
        this.minutesPassed = minutesPassed;
    }

    /**
     * Establece las horas transcurridas.
     * 
     * @param hoursPassed Las horas transcurridas.
     */
    public void setHoursPassed(int hoursPassed) {
        this.hoursPassed = hoursPassed;
    }

    /**
     * Obtiene el estado del temporizador.
     * 
     * @return true si el temporizador ha expirado, false en caso contrario.
     */
    public boolean isTimerExpired() {
        return timerExpired;
    }

    /**
     * Establece el estado del temporizador.
     * 
     * @param timerExpired El estado del temporizador.
     */
    public void setTimerExpired(boolean timerExpired) {
        this.timerExpired = timerExpired;
    }

    /**
     * Obtiene las horas transcurridas.
     * 
     * @return Las horas transcurridas.
     */
    public int getHoursPassed() {
        return hoursPassed;
    }

    /**
     * Obtiene los minutos transcurridos.
     * 
     * @return Los minutos transcurridos.
     */
    public int getMinutesPassed() {
        return minutesPassed;
    }

    /**
     * Obtiene los segundos transcurridos.
     * 
     * @return Los segundos transcurridos.
     */
    public int getSecondsPassed() {
        return secondsPassed;
    }

    /**
     * Obtiene el tipo de cronómetro.
     * 
     * @return El tipo de cronómetro.
     */
    public String getCronometro() {
        return cronometro;
    }

    /**
     * Establece el tipo de cronómetro.
     * 
     * @param cronometro El tipo de cronómetro.
     */
    public void setCronometro(String cronometro) {
        this.cronometro = cronometro;
    }


}
