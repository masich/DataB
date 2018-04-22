package entities;

import java.sql.Date;
import java.time.LocalTime;

public class Game {
    private int idGame;
    private Map map;
    private Date startDate;
    private LocalTime duration;
    private Schedule startTime;

    public int getIdGame() {
        return idGame;
    }

    public void setIdGame(int idGame) {
        this.idGame = idGame;
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public LocalTime getDuration() {
        return duration;
    }

    public void setDuration(LocalTime duration) {
        this.duration = duration;
    }

    public Schedule getStartTime() {
        return startTime;
    }

    public void setStartTime(Schedule startTime) {
        this.startTime = startTime;
    }
}
