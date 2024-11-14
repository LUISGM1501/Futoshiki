package controller.timer;

public class TimerController
{
    private int secondsPassed;
    private int minutesPassed;
    private int hoursPassed;
    private boolean cronometro;



    public TimerController(int secondsPassed, int minutesPassed, int hoursPassed, boolean cronometro)
    {
        this.secondsPassed = secondsPassed;
        this.minutesPassed = minutesPassed;
        this.hoursPassed = hoursPassed;
        this.cronometro = cronometro;
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

    //Comienza a actualizar el tiempo segundo por segundo
    public void actualizarTiempo()
    {
        if(!cronometro)
        {
            secondsPassed++;
            if(secondsPassed >= 60)
            {
                secondsPassed = 0;
                minutesPassed += 1;
            }
            if(minutesPassed >= 60)
            {
                hoursPassed +=1;
                minutesPassed = 0;
            }
        }else
        {
            secondsPassed--;
            if(secondsPassed <= 0)
            {
                secondsPassed = 59;
                minutesPassed -= 1;
            }
            if(minutesPassed <= 0)
            {
                hoursPassed -=1;
                minutesPassed = 59;
            }
        }

    }

}
