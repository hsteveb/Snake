package com.bignerdranch.android.animations;

public class SumTime {

    private long hours, minutes, seconds;

    public SumTime()
    {
        hours = minutes = seconds = 0;
    }


    /* converts the time from millis to human readable time
    * */
    public void addminute(long millis)
    {
        seconds = millis / 1000;

        if(seconds == 60)
        {
            seconds = 0;
            minutes += 1;

            if(minutes == 60)
            {
                minutes = 0;
                hours += 1;
            }
        }
    }


    /* Prints out the time in hours, minutes, and seconds */
    @Override
    public String toString() {
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
