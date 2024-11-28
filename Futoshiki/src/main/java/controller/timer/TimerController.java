package controller.timer;

import model.config.Configuration;

/**
 * Controlador para gestionar el temporizador del juego.
 */
public class TimerController
{
    private int secondsPassed;
    private int minutesPassed;
    private int hoursPassed;
    private String cronometro;

    /**
     * Establece el tipo de cronómetro.
     * 
     * @param cronometro El tipo de cronómetro a utilizar.
     */
    public void setCronometro(String cronometro)
    {
        this.cronometro = cronometro;
    }

    /**
     * Establece las horas transcurridas.
     * 
     * @param hoursPassed Las horas transcurridas.
     */
    public void setHoursPassed(int hoursPassed)
    {
        this.hoursPassed = hoursPassed;
    }

    /**
     * Establece los minutos transcurridos.
     * 
     * @param minutesPassed Los minutos transcurridos.
     */
    public void setMinutesPassed(int minutesPassed)
    {
        this.minutesPassed = minutesPassed;
    }

    /**
     * Establece los segundos transcurridos.
     * 
     * @param secondsPassed Los segundos transcurridos.
     */
    public void setSecondsPassed(int secondsPassed)
    {
        this.secondsPassed = secondsPassed;
    }

    /**
     * Devuelve una representación en cadena del tiempo transcurrido.
     * 
     * @return Una cadena en el formato "horas:minutos:segundos".
     */
    public String toString()
    {
        return hoursPassed + ":" + minutesPassed + ":" + secondsPassed;
    }

    /**
     * Obtiene las horas transcurridas.
     * 
     * @return Las horas transcurridas.
     */
    public int getHoursPassed()
    {
        return hoursPassed;
    }

    /**
     * Obtiene los minutos transcurridos.
     * 
     * @return Los minutos transcurridos.
     */
    public int getMinutesPassed()
    {
        return minutesPassed;
    }

    /**
     * Obtiene los segundos transcurridos.
     * 
     * @return Los segundos transcurridos.
     */
    public int getSecondsPassed()
    {
        return secondsPassed;
    }

    /**
     * Configura los valores del temporizador a partir de una configuración dada.
     * 
     * @param config La configuración que contiene los valores del temporizador.
     */
    public void setValores(Configuration config)
    {
        secondsPassed = config.getTimerSeconds();
        minutesPassed = config.getTimerMinutes();
        hoursPassed = config.getTimerHours();
        cronometro = config.getTimerType();
    }

    // Comienza a actualizar el tiempo segundo por segundo
    /**
     * Actualiza el tiempo transcurrido basado en el tipo de cronómetro.
     */
    public void actualizarTiempo()
    {
        switch (cronometro) {
            case "Cronómetro":
                secondsPassed++;
                if (secondsPassed >= 60) {
                    secondsPassed = 0;
                    minutesPassed += 1;
                }
                if (minutesPassed >= 60) {
                    hoursPassed += 1;
                    minutesPassed = 0;
                }
                break;
            case "Temporizador":
                secondsPassed--;
                if(secondsPassed == 0 && minutesPassed == 0 && hoursPassed == 0)
                {
                    cronometro = "TERMINADO";
                }else
                {
                    if (secondsPassed <= 0) {
                        secondsPassed = 59;
                        minutesPassed -= 1;
                    }
                    if (minutesPassed < 0) {
                        hoursPassed -= 1;
                        minutesPassed = 59;
                    }
                }
                break;

            default:
                //System.out.println("A");
                break;
        }
    }
}
