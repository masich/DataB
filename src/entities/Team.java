package entities;

public class Team {
    private int idTeam;
    private Client captain;
    private int playersCount;
    private Game game;

    public Client getCaptain() {
        return captain;
    }

    public void setCaptain(Client captain) {
        this.captain = captain;
    }

    public int getIdTeam() {
        return idTeam;
    }

    public void setIdTeam(int idTeam) {
        this.idTeam = idTeam;
    }

    public void setPlayersCount(int playersCount) {
        this.playersCount = playersCount;
    }

    public Integer getPlayersCount() {
        return playersCount;
    }

    public void setPlayersCount(Integer playersCount) {
        this.playersCount = playersCount;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}
