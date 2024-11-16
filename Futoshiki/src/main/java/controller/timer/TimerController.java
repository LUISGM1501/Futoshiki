package controller.timer;

import model.config.Configuration;

public class TimerController
{
    private int secondsPassed;
    private int minutesPassed;
    private int hoursPassed;
    private String cronometro;






    public void setCronometro(String cronometro)
    {
        this.cronometro = cronometro;
    }

    public void setHoursPassed(int hoursPassed)
    {
        this.hoursPassed = hoursPassed;
    }

    public void setMinutesPassed(int minutesPassed)
    {
        this.minutesPassed = minutesPassed;
    }

    public void setSecondsPassed(int secondsPassed)
    {
        this.secondsPassed = secondsPassed;
    }

    public String toString()
    {
        return hoursPassed + ":" + minutesPassed + ":" + secondsPassed;
    }

    public int getHoursPassed()
    {
        return hoursPassed;
    }

    public int getMinutesPassed()
    {
        return minutesPassed;
    }

    public int getSecondsPassed()
    {
        return secondsPassed;
    }


    public void setValores(Configuration config)
    {
        secondsPassed = config.getTimerSeconds();
        minutesPassed = config.getTimerMinutes();
        hoursPassed = config.getTimerHours();
        cronometro = config.getTimerType();
    }

    //Comienza a actualizar el tiempo segundo por segundo
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
                    if (secondsPassed == 0) {
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
