package model.timer;

import java.util.EventListener;

public class GameTimer {
    private int hours;
    private int minutes;
    private int seconds;
    private boolean isCountDown;
    private EventListener listener;

    /**
     * Obtiene las horas del temporizador.
     * 
     * @return Las horas del temporizador.
     */
    public int getHours() {
        return hours;
    }

    /**
     * Establece las horas del temporizador.
     * 
     * @param hours Las horas a establecer.
     */
    public void setHours(int hours) {
        this.hours = hours;
    }

    /**
     * Obtiene los minutos del temporizador.
     * 
     * @return Los minutos del temporizador.
     */
    public int getMinutes() {
        return minutes;
    }

    /**
     * Establece los minutos del temporizador.
     * 
     * @param minutes Los minutos a establecer.
     */
    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    /**
     * Obtiene los segundos del temporizador.
     * 
     * @return Los segundos del temporizador.
     */
    public int getSeconds() {
        return seconds;
    }

    /**
     * Establece los segundos del temporizador.
     * 
     * @param seconds Los segundos a establecer.
     */
    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    /**
     * Verifica si el temporizador es de cuenta regresiva.
     * 
     * @return true si es de cuenta regresiva, false en caso contrario.
     */
    public boolean isCountDown() {
        return isCountDown;
    }

    /**
     * Establece si el temporizador es de cuenta regresiva.
     * 
     * @param isCountDown true para cuenta regresiva, false en caso contrario.
     */
    public void setCountDown(boolean isCountDown) {
        this.isCountDown = isCountDown;
    }

    /**
     * Obtiene el listener del temporizador.
     * 
     * @return El listener del temporizador.
     */
    public EventListener getListener() {
        return listener;
    }

    /**
     * Establece el listener del temporizador.
     * 
     * @param listener El listener a establecer.
     */
    public void setListener(EventListener listener) {
        this.listener = listener;
    }
}