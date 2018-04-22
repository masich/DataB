package entities;

import java.sql.Time;
import java.time.LocalTime;

public enum Schedule {
    //Games start at 10:00 and finish at 18:00
    GAME_1(Time.valueOf(LocalTime.of(10, 0))),
    GAME_2(Time.valueOf(LocalTime.of(11, 0))),
    GAME_3(Time.valueOf(LocalTime.of(12, 0))),
    GAME_4(Time.valueOf(LocalTime.of(13, 0))),
    GAME_5(Time.valueOf(LocalTime.of(14, 0))),
    GAME_6(Time.valueOf(LocalTime.of(15, 0))),
    GAME_7(Time.valueOf(LocalTime.of(16, 0))),
    GAME_8(Time.valueOf(LocalTime.of(17, 0)));


    private Time startTime;

    Schedule(Time startTime) {
        this.startTime = startTime;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }
}
