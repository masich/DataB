package entities;

import entities.base.Entity;
import entities.base.annotations.Field;
import entities.base.annotations.ForeignKey;
import entities.base.annotations.PrimaryKey;
import entities.base.annotations.Table;

import java.sql.Date;
import java.time.LocalTime;

@Table("GAMES")
public class Game extends Entity {
    @PrimaryKey("id_game")
    private int idGame;
    @ForeignKey("ref_map")
    private Map map;
    @Field("start_date")
    private Date startDate;
    @Field("duration")
    private LocalTime duration;
//    @ForeignKey("start_time")
    private Schedule startTime = Schedule.GAME_1;

    private Game() {
    }

    public Game(int idGame, Map map, Date startDate, LocalTime duration) {
        this.idGame = idGame;
        this.map = map;
        this.startDate = startDate;
        this.duration = duration;
    }

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
